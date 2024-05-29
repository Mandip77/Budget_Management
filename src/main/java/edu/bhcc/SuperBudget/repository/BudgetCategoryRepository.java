package edu.bhcc.SuperBudget.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.bhcc.SuperBudget.model.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, Long> {
}


