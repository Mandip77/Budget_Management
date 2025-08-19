package edu.bhcc.SuperBudget.service;

import edu.bhcc.SuperBudget.model.BudgetCategory;
import edu.bhcc.SuperBudget.model.Transaction;
import edu.bhcc.SuperBudget.repository.BudgetCategoryRepository;
import edu.bhcc.SuperBudget.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetCategoryRepository budgetCategoryRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public BudgetService(
            BudgetCategoryRepository budgetCategoryRepository,
            TransactionRepository transactionRepository
    ) {
        this.budgetCategoryRepository = budgetCategoryRepository;
        this.transactionRepository = transactionRepository;
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
        updateTransaction(id, newAmount, null, null);
    }

    // NEW: Enhanced update method with description and category support
    public void updateTransaction(Long id, Double newAmount, String newDescription, BudgetCategory newBudgetCategory) {
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
                    throw new IllegalArgumentException("Insufficient balance to increase transaction amount. Available: $"
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

    public BudgetCategory updateBudgetCategory(BudgetCategory updatedCategory, Double previousAllocation) {
        // Load the existing category from the DB
        BudgetCategory existing = budgetCategoryRepository.findById(updatedCategory.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Update editable fields
        existing.setName(updatedCategory.getName());
        existing.setAllocation(updatedCategory.getAllocation());

        // Compute allocation change and adjust balances
        double allocationChange = updatedCategory.getAllocation() - previousAllocation;
        existing.setRemainingAmount(existing.getRemainingAmount() + allocationChange);
        existing.setBalance(existing.getBalance() + allocationChange);

        return budgetCategoryRepository.save(existing);
    }

}