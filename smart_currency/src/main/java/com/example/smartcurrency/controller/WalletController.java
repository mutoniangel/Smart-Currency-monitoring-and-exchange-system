package com.example.smartcurrency.controller;

import com.example.smartcurrency.model.Wallet;
import com.example.smartcurrency.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/my")
    public ResponseEntity<List<Wallet>> getMyWallets(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(walletService.getWalletsByUser(username));
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(Authentication authentication, @RequestBody Map<String, Object> request) {
        String username = authentication.getName();
        String currencyCode = (String) request.get("currencyCode");
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        Wallet wallet = walletService.deposit(username, currencyCode, amount);
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(Authentication authentication, @RequestBody Map<String, Object> request) {
        String username = authentication.getName();
        String currencyCode = (String) request.get("currencyCode");
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        Wallet wallet = walletService.withdraw(username, currencyCode, amount);
        return ResponseEntity.ok(wallet);
    }
}
