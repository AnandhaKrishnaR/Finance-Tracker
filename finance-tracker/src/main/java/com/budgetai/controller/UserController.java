package com.budgetai.controller;

import com.budgetai.model.User;
import com.budgetai.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "profile";
    }
    
    @PostMapping("/profile")
    public String updateProfile(@RequestParam String name,
                               @RequestParam String email,
                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        
        // Update user information
        user.setName(name);
        user.setEmail(email);
        
        // Note: In a real application, you'd want to update this in the database
        // For now, we'll just redirect back to profile
        
        return "redirect:/profile?success=true";
    }
    
    @GetMapping("/settings")
    public String settings(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "settings";
    }
    
    @PostMapping("/settings")
    public String changePassword(@RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Authentication authentication,
                                Model model) {
        User user = (User) authentication.getPrincipal();
        
        // Validate current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "Current password is incorrect");
            return "settings";
        }
        
        // Validate new password
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match");
            return "settings";
        }
        
        if (newPassword.length() < 6) {
            model.addAttribute("error", "New password must be at least 6 characters long");
            return "settings";
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        
        return "redirect:/settings?success=true";
    }
}








