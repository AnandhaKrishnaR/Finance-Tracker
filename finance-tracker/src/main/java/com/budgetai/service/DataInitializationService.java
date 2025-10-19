package com.budgetai.service;

import com.budgetai.model.Category;
import com.budgetai.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Override
    public void run(String... args) throws Exception {
        initializeCategories();
    }
    
    private void initializeCategories() {
        if (categoryRepository.count() == 0) {
            System.out.println("Initializing categories...");
            
            List<String> categoryNames = Arrays.asList(
                "Grocery", "Gas", "Entertainment", "Netflix", "Pharmacy", "Utilities",
                "Restaurant", "Transportation", "Shopping", "Healthcare", "Education", "Travel"
            );
            
            for (String name : categoryNames) {
                Category category = new Category();
                category.setName(name);
                categoryRepository.save(category);
                System.out.println("Created category: " + name);
            }
            
            System.out.println("Categories initialized successfully!");
        } else {
            System.out.println("Categories already exist: " + categoryRepository.count() + " categories found");
        }
    }
}







