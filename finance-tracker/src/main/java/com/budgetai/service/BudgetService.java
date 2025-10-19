package com.budgetai.service;

import com.budgetai.model.Budget;
import com.budgetai.model.User;
import com.budgetai.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@Service
public class BudgetService {
    
    private final BudgetRepository budgetRepository;
    
    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }
    
    public void createDefaultBudget(User user) {
        // Create a default budget for the current month
        YearMonth currentMonth = YearMonth.now();
        LocalDate monthStart = currentMonth.atDay(1);
        
        Budget budget = new Budget();
        budget.setUser(user);
        budget.setMonthYear(monthStart);
        budget.setAmount(3000.0); // Default budget amount
        
        budgetRepository.save(budget);
    }
    
    public Budget getMonthlyBudget(User user, YearMonth yearMonth) {
        return budgetRepository.findFirstByUserAndMonthYear(user, yearMonth.atDay(1))
            .orElse(null);
    }
    
    public void createOrUpdateBudget(User user, LocalDate monthStart, Double amount) {
        Optional<Budget> existingBudget = budgetRepository.findFirstByUserAndMonthYear(user, monthStart);
        
        if (existingBudget.isPresent()) {
            Budget budget = existingBudget.get();
            budget.setAmount(amount);
            budgetRepository.save(budget);
        } else {
            Budget budget = new Budget();
            budget.setUser(user);
            budget.setMonthYear(monthStart);
            budget.setAmount(amount);
            budgetRepository.save(budget);
        }
    }
}
