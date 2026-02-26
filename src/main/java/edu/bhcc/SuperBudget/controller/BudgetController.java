package edu.bhcc.SuperBudget.controller;

import edu.bhcc.SuperBudget.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import edu.bhcc.SuperBudget.model.BudgetCategory;
import edu.bhcc.SuperBudget.service.BudgetService;

@Controller
public class BudgetController {

    private final BudgetService budgetService;

    @Autowired
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/")
    public String index(Model model) {
        java.util.List<BudgetCategory> categories = budgetService.findAllBudgetCategories();
        double totalAllocated = categories.stream().mapToDouble(BudgetCategory::getAllocation).sum();
        double totalSpent = categories.stream().mapToDouble(c -> c.getAllocation() - c.getBalance()).sum();

        Double globalBudgetSetting = budgetService.getGlobalBudget();
        double displayTotalBudget = (globalBudgetSetting != null) ? globalBudgetSetting : totalAllocated;
        double displayTotalRemaining = displayTotalBudget - totalSpent;

        model.addAttribute("budgetCategories", categories);
        // Pass the display budget (either manual or sum)
        model.addAttribute("totalBudget", displayTotalBudget);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("totalRemaining", displayTotalRemaining);
        // Pass the actual allocated sum
        model.addAttribute("totalAllocated", totalAllocated);
        model.addAttribute("globalBudgetSetting", globalBudgetSetting);

        model.addAttribute("transactions", budgetService.findAllTransactions());
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("budgetCategory", new BudgetCategory());
        return "index";
    }

    @PostMapping("/add-transaction")
    public String addTransaction(
            @ModelAttribute Transaction transaction,
            @RequestParam("categoryId") Long budgetCategoryId,
            Model model) {
        BudgetCategory budgetCategory = budgetService.findBudgetCategoryById(budgetCategoryId);
        if (budgetCategory != null) {
            try {
                if (transaction.getDate() == null) {
                    transaction.setDate(java.time.LocalDate.now());
                }
                transaction.setBudgetCategory(budgetCategory);
                budgetService.saveTransaction(transaction);
                System.out.println("Added transaction: " + transaction); // Debug logging
            } catch (IllegalArgumentException e) {
                System.out.println("Error adding transaction: " + e.getMessage()); // Debug logging
                model.addAttribute("errorMessage", "Transaction could not be completed: " + e.getMessage());
                return "redirect:/";
            }
        } else {
            System.out.println("Invalid budget category."); // Debug logging
            throw new IllegalArgumentException("Invalid budget category.");
        }
        return "redirect:/";
    }

    // NEW: Get edit transaction form
    @GetMapping("/edit-transaction/{id}")
    public String editTransactionForm(@PathVariable("id") Long id, Model model) {
        Transaction transaction = budgetService.findTransactionById(id);
        if (transaction == null) {
            model.addAttribute("errorMessage", "Transaction not found.");
            return "redirect:/";
        }

        model.addAttribute("transaction", transaction);
        model.addAttribute("budgetCategories", budgetService.findAllBudgetCategories());
        return "edit-transaction";
    }

    // UPDATED: Edit transaction method with proper validation and category support
    @PostMapping("/edit-transaction")
    public String editTransaction(
            @ModelAttribute Transaction transaction,
            @RequestParam(value = "categoryId", required = false) Long budgetCategoryId,
            Model model) {
        try {
            BudgetCategory budgetCategory = null;
            if (budgetCategoryId != null) {
                budgetCategory = budgetService.findBudgetCategoryById(budgetCategoryId);
                if (budgetCategory == null) {
                    model.addAttribute("errorMessage", "Invalid budget category selected.");
                    return "redirect:/edit-transaction/" + transaction.getId();
                }
            }

            budgetService.updateTransaction(transaction.getId(), transaction.getAmount(),
                    transaction.getDescription(), budgetCategory, transaction.getDate());
            System.out.println("Updated transaction: " + transaction); // Debug logging
        } catch (IllegalArgumentException e) {
            System.out.println("Error updating transaction: " + e.getMessage()); // Debug logging
            model.addAttribute("errorMessage", "Transaction could not be updated: " + e.getMessage());
            return "redirect:/edit-transaction/" + transaction.getId();
        }
        return "redirect:/";
    }

    @PostMapping("/add-category")
    public String addCategory(@ModelAttribute BudgetCategory budgetCategory) {
        budgetCategory.setBalance(budgetCategory.getAllocation());
        budgetCategory.setRemainingAmount(budgetCategory.getAllocation());
        budgetService.saveBudgetCategory(budgetCategory);
        return "redirect:/";
    }

    @GetMapping("/delete-category/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        budgetService.deleteBudgetCategory(id);
        return "redirect:/";
    }

    @GetMapping("/edit-category/{id}")
    public String editCategoryForm(@PathVariable("id") Long id, Model model) {
        BudgetCategory budgetCategory = budgetService.findBudgetCategoryById(id);
        model.addAttribute("budgetCategory", budgetCategory);
        return "edit-category";
    }

    @PostMapping("/edit-category")
    public String editCategory(
            @ModelAttribute BudgetCategory budgetCategory,
            @RequestParam("previousAllocation") Double previousAllocation) {
        budgetService.updateBudgetCategory(budgetCategory, previousAllocation);
        return "redirect:/";
    }

    @GetMapping("/delete-transaction/{id}")
    public String deleteTransaction(@PathVariable("id") Long id) {
        budgetService.deleteTransaction(id);
        return "redirect:/";
    }

    @PostMapping("/reset-month")
    public String resetMonth() {
        budgetService.resetMonthlyBudget();
        return "redirect:/";
    }

    // --- Phase 4: Global Budget Endpoint ---
    @PostMapping("/set-global-budget")
    public String setGlobalBudget(@RequestParam("globalBudgetAmount") Double amount, Model model) {
        try {
            budgetService.setGlobalBudget(amount);
            System.out.println("Set global budget to: " + amount);
        } catch (IllegalArgumentException e) {
            System.out.println("Error setting global budget: " + e.getMessage());
        }
        return "redirect:/";
    }
}