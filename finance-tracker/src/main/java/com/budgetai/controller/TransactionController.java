package com.budgetai.controller;

import com.budgetai.model.Category;
import com.budgetai.model.Transaction;
import com.budgetai.model.User;
import com.budgetai.repository.CategoryRepository;
import com.budgetai.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TransactionController {
    
    private final TransactionService transactionService;
    private final CategoryRepository categoryRepository;
    
    public TransactionController(TransactionService transactionService, CategoryRepository categoryRepository) {
        this.transactionService = transactionService;
        this.categoryRepository = categoryRepository;
    }
    
    @GetMapping("/transactions")
    public String list(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("transactions", transactionService.getRecentTransactions(user, 50));
        model.addAttribute("categories", categoryRepository.findAll());
        return "transactions";
    }
    
    @PostMapping("/transactions/add")
    public String addTransaction(@RequestParam Double amount,
                               @RequestParam Long categoryId,
                               @RequestParam String description,
                               @RequestParam LocalDate date,
                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        transactionService.addTransaction(user, categoryId, amount, description, date);
        return "redirect:/dashboard";
    }

    @PostMapping("/transactions/delete/{id}")
    public String delete(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        transactionService.deleteTransactionById(id, user);
        return "redirect:/transactions";
    }
}








