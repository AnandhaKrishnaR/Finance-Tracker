package com.budgetai.repository;

import com.budgetai.model.Budget;
import com.budgetai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findFirstByUserAndMonthYear(User user, LocalDate monthYear);
}











