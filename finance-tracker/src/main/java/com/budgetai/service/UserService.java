package com.budgetai.service;

import com.budgetai.model.User;
import com.budgetai.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BudgetService budgetService;
    
    public UserService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder,
                      BudgetService budgetService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.budgetService = budgetService;
    }
    
    public User registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        
        User savedUser = userRepository.save(user);
        
        // Create default budget for the new user
        budgetService.createDefaultBudget(savedUser);
        
        return savedUser;
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}








