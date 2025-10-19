package com.budgetai.controller;

import com.budgetai.model.Category;
import com.budgetai.model.User;
import com.budgetai.repository.CategoryRepository;
import com.budgetai.service.DashboardService;
import com.budgetai.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; // NEW: Import RequestParam

import java.time.YearMonth;
import java.time.format.DateTimeFormatter; // NEW: Import DateTimeFormatter
import java.util.List;
import java.util.Locale; // NEW: Import Locale for formatting month name
import java.util.Map;

@Controller
public class DashboardController {
    private final DashboardService dashboardService;
    private final TransactionService transactionService;
    private final CategoryRepository categoryRepository;

    public DashboardController(DashboardService dashboardService,
                               TransactionService transactionService,
                               CategoryRepository categoryRepository) {
        this.dashboardService = dashboardService;
        this.transactionService = transactionService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model, Authentication authentication,
                            // NEW: Accept an optional 'month' parameter (e.g., "2025-09").
                            @RequestParam(value = "month", required = false) String monthStr) {
                                
        User user = (User) authentication.getPrincipal();

        // NEW: Determine the month to display. Default to the current month if none is provided.
        YearMonth ym = (monthStr != null && !monthStr.isEmpty())
                ? YearMonth.parse(monthStr, DateTimeFormatter.ofPattern("yyyy-MM"))
                : YearMonth.now();

        // --- All your existing service calls will now use the dynamic 'ym' object ---
        double spent = dashboardService.getMonthlySpent(user, ym);
        double budget = dashboardService.getMonthlyBudget(user, ym);
        double remainingPct = dashboardService.getBudgetRemainingPercent(user, ym);
        Map<String, Double> chartData = dashboardService.getMonthlyCategoryChart(user, ym);

        List<Category> categories = categoryRepository.findAll();
        
        // NEW: The month title now includes the year for clarity when navigating.
        model.addAttribute("monthTitle", ym.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)));
        
        model.addAttribute("spent", spent);
        model.addAttribute("budget", budget);
        model.addAttribute("remainingPct", remainingPct);
        model.addAttribute("chartLabels", chartData.keySet());
        model.addAttribute("chartValues", chartData.values());
        model.addAttribute("recentTransactions", transactionService.getRecentTransactions(user,5));
        model.addAttribute("categories", categories);

     
        String previousMonth = ym.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String nextMonth = ym.plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        model.addAttribute("previousMonth", previousMonth);
        model.addAttribute("nextMonth", nextMonth);
        
        // NEW: Add a flag to disable the 'next' button for the current month or future months.
        model.addAttribute("isCurrentOrFutureMonth", !ym.isBefore(YearMonth.now()));

        return "dashboard";
    }
    
    @GetMapping("/insights")
    public String insights() {
        return "insights";
    }
}





