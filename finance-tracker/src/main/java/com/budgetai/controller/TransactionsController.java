package com.budgetai.controller;

import com.budgetai.model.User;
import com.budgetai.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.YearMonth;

@Controller
public class TransactionsController {
    private final TransactionService transactionService;

    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transaction")
    public String list(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("transactions", transactionService.getRecentTransactions(user, 50));
        model.addAttribute("monthTitle", YearMonth.now().getMonth().name());
        return "transactions";
    }
}



