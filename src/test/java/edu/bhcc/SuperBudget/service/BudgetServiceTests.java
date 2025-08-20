package edu.bhcc.SuperBudget.service;

import edu.bhcc.SuperBudget.model.BudgetCategory;
import edu.bhcc.SuperBudget.model.Transaction;
import edu.bhcc.SuperBudget.repository.BudgetCategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BudgetServiceTests {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private BudgetCategoryRepository budgetCategoryRepository;

    @Test
    public void saveTransactionDecreasesBalanceAndRemainingAmount() {
        BudgetCategory category = new BudgetCategory();
        category.setName("Food");
        category.setAllocation(100.0);
        category.setBalance(100.0);
        category.setRemainingAmount(100.0);
        budgetCategoryRepository.save(category);

        Transaction transaction = new Transaction("Groceries", 40.0, category);

        budgetService.saveTransaction(transaction);

        BudgetCategory updated = budgetCategoryRepository.findById(category.getId()).orElseThrow();
        assertEquals(60.0, updated.getBalance());
        assertEquals(60.0, updated.getRemainingAmount());
    }

    @Test
    public void saveTransactionThrowsWhenInvalid() {
        BudgetCategory category = new BudgetCategory();
        category.setName("Utilities");
        category.setAllocation(50.0);
        category.setBalance(50.0);
        category.setRemainingAmount(50.0);
        budgetCategoryRepository.save(category);

        Transaction tooLarge = new Transaction("Big bill", 60.0, category);
        assertThrows(IllegalArgumentException.class, () -> budgetService.saveTransaction(tooLarge));

        Transaction noCategory = new Transaction("Uncategorized", 10.0, null);
        assertThrows(IllegalArgumentException.class, () -> budgetService.saveTransaction(noCategory));
    }
}

