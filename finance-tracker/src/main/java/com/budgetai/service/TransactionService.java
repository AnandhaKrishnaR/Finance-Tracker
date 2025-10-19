package com.budgetai.service;

import com.budgetai.model.Category;
import com.budgetai.model.Transaction;
import com.budgetai.model.User;
import com.budgetai.repository.CategoryRepository;
import com.budgetai.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Transaction> getRecentTransactions(User user, int limit) {
        return transactionRepository.findTop5ByUserOrderByDateDesc(user);
    }

    public double getMonthlySpending(User user, YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        Double sum = transactionRepository.sumByUserAndDateBetween(user, start, end);
        return sum == null ? 0.0 : sum;
    }

    public Map<String, Double> getMonthlyCategoryTotals(User user, YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        List<Object[]> rows = transactionRepository.sumByCategoryForPeriod(user, start, end);
        Map<String, Double> map = new HashMap<>();
        for (Object[] row : rows) {
            map.put((String) row[0], ((Number) row[1]).doubleValue());
        }
        return map;
    }

    public void addTransaction(User user, Long categoryId, Double amount, String description, LocalDate date) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setDate(date);
        
        transactionRepository.save(transaction);
    }

    public void deleteTransactionById(Long id, User user) {
        Transaction tx = transactionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + id));
        if (!tx.getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("Not allowed");
        }
        transactionRepository.delete(tx);
    }
}



