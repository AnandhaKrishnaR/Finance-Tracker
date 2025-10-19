package com.budgetai.service;

import com.budgetai.model.Budget;
import com.budgetai.model.User;
import com.budgetai.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Map;
import java.util.Optional;

@Service
public class DashboardService {
    private final TransactionService transactionService;
    private final BudgetRepository budgetRepository;

    public DashboardService(TransactionService transactionService, BudgetRepository budgetRepository) {
        this.transactionService = transactionService;
        this.budgetRepository = budgetRepository;
    }

    public double getMonthlySpent(User user, YearMonth ym) {
        return transactionService.getMonthlySpending(user, ym);
    }

    public double getMonthlyBudget(User user, YearMonth ym) {
        Optional<Budget> b = budgetRepository.findFirstByUserAndMonthYear(user, ym.atDay(1));
        return b.map(Budget::getAmount).orElse(0.0);
    }

    public double getBudgetRemainingPercent(User user, YearMonth ym) {
        double budget = getMonthlyBudget(user, ym);
        if (budget <= 0.0) return 0.0;
        double spent = getMonthlySpent(user, ym);
        double remaining = Math.max(budget - spent, 0.0);
        return (remaining / budget) * 100.0;
    }

    public Map<String, Double> getMonthlyCategoryChart(User user, YearMonth ym) {
        return transactionService.getMonthlyCategoryTotals(user, ym);
    }
}











