package edu.bhcc.SuperBudget.service;

import edu.bhcc.SuperBudget.model.BudgetCategory;
import edu.bhcc.SuperBudget.model.Transaction;
import edu.bhcc.SuperBudget.model.AppSetting;
import edu.bhcc.SuperBudget.repository.BudgetCategoryRepository;
import edu.bhcc.SuperBudget.repository.TransactionRepository;
import edu.bhcc.SuperBudget.repository.AppSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;

@Service
public class BudgetService {

    private final BudgetCategoryRepository budgetCategoryRepository;
    private final TransactionRepository transactionRepository;
    private final AppSettingRepository appSettingRepository;

    @Autowired
    public BudgetService(
            BudgetCategoryRepository budgetCategoryRepository,
            TransactionRepository transactionRepository,
            AppSettingRepository appSettingRepository) {
        this.budgetCategoryRepository = budgetCategoryRepository;
        this.transactionRepository = transactionRepository;
        this.appSettingRepository = appSettingRepository;
    }

    public List<BudgetCategory> findAllBudgetCategories() {
        return budgetCategoryRepository.findAll();
    }

    public void deleteBudgetCategory(Long id) {
        budgetCategoryRepository.deleteById(id);
    }

    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<BudgetCategory> getAllBudgetCategories() {
        return budgetCategoryRepository.findAll();
    }

    public BudgetCategory saveBudgetCategory(BudgetCategory budgetCategory) {
        return budgetCategoryRepository.save(budgetCategory);
    }

    public BudgetCategory updateBudgetCategory(BudgetCategory budgetCategory) {
        return budgetCategoryRepository.save(budgetCategory);
    }

    public BudgetCategory findBudgetCategoryById(Long id) {
        return budgetCategoryRepository.findById(id).orElse(null);
    }

    // NEW: Find transaction by ID
    public Transaction findTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public void saveTransaction(Transaction transaction) {
        BudgetCategory budgetCategory = transaction.getBudgetCategory();
        if (budgetCategory != null) {
            budgetCategory.updateBalanceAndActivity(transaction.getAmount());
            transactionRepository.save(transaction);
            budgetCategoryRepository.save(budgetCategory);
        } else {
            throw new IllegalArgumentException("Budget category is required for the transaction.");
        }
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction != null) {
            BudgetCategory budgetCategory = transaction.getBudgetCategory();
            if (budgetCategory != null) {
                Double remainingAmount = budgetCategory.getRemainingAmount() + transaction.getAmount();
                Double newBalance = budgetCategory.getBalance() + transaction.getAmount();

                budgetCategory.setRemainingAmount(remainingAmount);
                budgetCategory.setBalance(newBalance);
                budgetCategoryRepository.save(budgetCategory);
            }
            transactionRepository.delete(transaction);
        }
    }

    // UPDATED: Original method kept for backward compatibility
    public void updateTransaction(Long id, Double newAmount) {
        updateTransaction(id, newAmount, null, null, null);
    }

    // NEW: Enhanced update method with description, category, and date support
    public void updateTransaction(Long id, Double newAmount, String newDescription, BudgetCategory newBudgetCategory,
            LocalDate newDate) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found with ID: " + id);
        }

        BudgetCategory oldBudgetCategory = transaction.getBudgetCategory();
        Double oldAmount = transaction.getAmount();

        // Validate new amount against new budget category if provided
        BudgetCategory targetCategory = newBudgetCategory != null ? newBudgetCategory : oldBudgetCategory;
        if (targetCategory != null && newAmount > 0) {
            // If changing categories or amounts, check if new category has enough balance
            if (newBudgetCategory != null && !newBudgetCategory.equals(oldBudgetCategory)) {
                // Moving to different category - check if target has enough balance
                if (newAmount > targetCategory.getBalance()) {
                    throw new IllegalArgumentException("Insufficient balance in target category. Available: $"
                            + String.format("%.2f", targetCategory.getBalance()) + ", Required: $"
                            + String.format("%.2f", newAmount));
                }
            } else if (newBudgetCategory == null || newBudgetCategory.equals(oldBudgetCategory)) {
                // Same category - check if the difference can be accommodated
                Double amountDifference = newAmount - oldAmount;
                if (amountDifference > 0 && amountDifference > targetCategory.getBalance()) {
                    throw new IllegalArgumentException(
                            "Insufficient balance to increase transaction amount. Available: $"
                                    + String.format("%.2f", targetCategory.getBalance()) + ", Additional required: $"
                                    + String.format("%.2f", amountDifference));
                }
            }
        }

        // Update old budget category (restore the old amount)
        if (oldBudgetCategory != null) {
            Double restoredBalance = oldBudgetCategory.getBalance() + oldAmount;
            Double restoredRemainingAmount = oldBudgetCategory.getRemainingAmount() + oldAmount;

            oldBudgetCategory.setBalance(restoredBalance);
            oldBudgetCategory.setRemainingAmount(restoredRemainingAmount);
            budgetCategoryRepository.save(oldBudgetCategory);
        }

        // Update the transaction
        transaction.setAmount(newAmount);
        if (newDescription != null && !newDescription.trim().isEmpty()) {
            transaction.setDescription(newDescription.trim());
        }
        if (newBudgetCategory != null) {
            transaction.setBudgetCategory(newBudgetCategory);
        }
        if (newDate != null) {
            transaction.setDate(newDate);
        }

        // Update target budget category (deduct the new amount)
        BudgetCategory finalTargetCategory = transaction.getBudgetCategory();
        if (finalTargetCategory != null) {
            try {
                finalTargetCategory.updateBalanceAndActivity(newAmount);
                budgetCategoryRepository.save(finalTargetCategory);
            } catch (IllegalArgumentException e) {
                // Restore old category if final update fails
                if (oldBudgetCategory != null) {
                    Double oldBalance = oldBudgetCategory.getBalance() - oldAmount;
                    Double oldRemainingAmount = oldBudgetCategory.getRemainingAmount() - oldAmount;
                    oldBudgetCategory.setBalance(oldBalance);
                    oldBudgetCategory.setRemainingAmount(oldRemainingAmount);
                    budgetCategoryRepository.save(oldBudgetCategory);
                }
                throw e;
            }
        }

        transactionRepository.save(transaction);
        System.out.println("Transaction updated successfully: ID=" + id + ", Amount=" + newAmount
                + ", Category=" + (finalTargetCategory != null ? finalTargetCategory.getName() : "None"));
    }

    /**
     * Update an existing budget category by loading it from the database,
     * updating its name and allocation, and adjusting its remaining amount
     * and balance based on the change in allocation.
     *
     * @param updatedCategory    the category submitted from the form (contains id,
     *                           name and allocation)
     * @param previousAllocation the allocation value prior to the edit
     * @return the saved BudgetCategory entity with updated values
     */
    public BudgetCategory updateBudgetCategory(BudgetCategory updatedCategory, Double previousAllocation) {
        // Fetch the existing category from the repository
        BudgetCategory existing = budgetCategoryRepository.findById(updatedCategory.getId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Category not found with ID: " + updatedCategory.getId()));

        // Update editable fields
        existing.setName(updatedCategory.getName());
        existing.setAllocation(updatedCategory.getAllocation());

        // Calculate the difference between the new and previous allocations
        double allocationChange = updatedCategory.getAllocation() - previousAllocation;

        // Adjust remaining amount and balance based on the allocation change
        existing.setRemainingAmount(existing.getRemainingAmount() + allocationChange);
        existing.setBalance(existing.getBalance() + allocationChange);

        // Persist and return the updated entity
        return budgetCategoryRepository.save(existing);
    }

    @jakarta.transaction.Transactional
    public void resetMonthlyBudget() {
        // Delete all transactions
        transactionRepository.deleteAll();

        // Reset all category balances to their allocations
        List<BudgetCategory> categories = budgetCategoryRepository.findAll();
        for (BudgetCategory category : categories) {
            category.setBalance(category.getAllocation());
            category.setRemainingAmount(category.getAllocation());
            category.setActivity(0.0);
            budgetCategoryRepository.save(category);
        }
    }

    // --- Phase 4: App Settings & Global Budget ---

    public Double getGlobalBudget() {
        return appSettingRepository.findById("GLOBAL_BUDGET")
                .map(setting -> Double.parseDouble(setting.getValue()))
                .orElse(null); // Return null if not set
    }

    public void setGlobalBudget(Double amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Global Budget must be a positive number.");
        }
        AppSetting setting = new AppSetting("GLOBAL_BUDGET", amount.toString());
        appSettingRepository.save(setting);
    }
}