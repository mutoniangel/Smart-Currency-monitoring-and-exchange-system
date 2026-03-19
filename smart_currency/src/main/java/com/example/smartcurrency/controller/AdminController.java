package com.example.smartcurrency.controller;

import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.model.User;
import com.example.smartcurrency.repository.CurrencyRepository;
import com.example.smartcurrency.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(u -> u.setPassword(null)); // Security
        return ResponseEntity.ok(users);
    }

    @PutMapping("/rates")
    public ResponseEntity<String> updateRate(@RequestBody Currency rateUpdate) {
        return currencyRepository.findById(rateUpdate.getCurrencyCode())
                .map(curr -> {
                    curr.setCurrentRate(rateUpdate.getCurrentRate());
                    curr.setTrend(rateUpdate.getTrend());
                    currencyRepository.save(curr);
                    return ResponseEntity.ok("Rate updated successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
