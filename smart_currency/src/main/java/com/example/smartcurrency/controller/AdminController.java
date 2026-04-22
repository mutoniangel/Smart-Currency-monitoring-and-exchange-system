package com.example.smartcurrency.controller;

import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.model.Transaction;
import com.example.smartcurrency.model.User;
import com.example.smartcurrency.repository.CurrencyRepository;
import com.example.smartcurrency.repository.TransactionRepository;
import com.example.smartcurrency.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private com.example.smartcurrency.service.CurrencyRateService currencyRateService;

    @Autowired
    private com.example.smartcurrency.service.CurrencyService currencyService;

    @PostMapping("/sync")
    public ResponseEntity<String> manualSync() {
        currencyRateService.refreshRates();
        return ResponseEntity.ok("External API Sync triggered successfully.");
    }

    @PostMapping("/seed")
    public ResponseEntity<String> seedCurrencies() {
        try {
            currencyService.seedCurrencies();
            currencyRateService.refreshRates();
            return ResponseEntity.ok("20+ Currencies seeded and synced successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during seeding: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> result = users.stream().map(u -> {
            Map<String, Object> map = new HashMap<>();
            map.put("userID", u.getUserID());
            map.put("username", u.getUsername());
            map.put("email", u.getEmail());
            map.put("role", u.getRole());
            map.put("enabled", u.isEnabled());
            return map;
        }).toList();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/users/{userId}/toggle")
    public ResponseEntity<String> toggleUserEnabled(@PathVariable Integer userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setEnabled(!user.isEnabled());
                    userRepository.save(user);
                    return ResponseEntity.ok("User " + user.getUsername() + " is now " + (user.isEnabled() ? "enabled" : "disabled"));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/rates")
    public ResponseEntity<String> updateRate(@Valid @RequestBody Currency rateUpdate) {
        return currencyRepository.findById(rateUpdate.getCurrencyCode())
                .map(curr -> {
                    curr.setCurrentRate(rateUpdate.getCurrentRate());
                    curr.setTrend(rateUpdate.getTrend());
                    currencyRepository.save(curr);
                    return ResponseEntity.ok("Rate updated successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalCurrencies", currencyRepository.count());
        stats.put("totalTransactions", transactionRepository.count());
        return ResponseEntity.ok(stats);
    }
}
