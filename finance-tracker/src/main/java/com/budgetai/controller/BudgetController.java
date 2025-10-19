package com.budgetai.controller;

import com.budgetai.model.Budget;
import com.budgetai.model.User;
import com.budgetai.service.BudgetService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@Controller
public class BudgetController {
    
    private final BudgetService budgetService;
    
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }
    
    @GetMapping("/budgets")
    public String budgets(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        YearMonth currentMonth = YearMonth.now();
        LocalDate monthStart = currentMonth.atDay(1);
        
        Budget currentBudget = budgetService.getMonthlyBudget(user, currentMonth);
        
        model.addAttribute("currentBudget", currentBudget);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("monthStart", monthStart);
        
        return "budgets";
    }
    
    @PostMapping("/budgets")
    public String updateBudget(@RequestParam Double amount,
                               @RequestParam String monthYear,
                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        LocalDate monthStart = LocalDate.parse(monthYear + "-01");
        
        budgetService.createOrUpdateBudget(user, monthStart, amount);
        
        return "redirect:/budgets?success=true";
    }
}








