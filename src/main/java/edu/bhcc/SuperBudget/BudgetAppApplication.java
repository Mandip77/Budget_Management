package edu.bhcc.SuperBudget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BudgetAppApplication {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(10000); // Add delay of 10 seconds
        SpringApplication.run(BudgetAppApplication.class, args);
    }
}
