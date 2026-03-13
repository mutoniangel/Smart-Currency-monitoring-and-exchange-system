package com.example.smartcurrency.controller;

import com.example.smartcurrency.model.Wallet;
import com.example.smartcurrency.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping("/user/{userID}")
    public ResponseEntity<List<Wallet>> getWallets(@PathVariable Integer userID) {
        return ResponseEntity.ok(walletService.getUserWallets(userID));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Wallet> deposit(@RequestBody Map<String, Object> request) {
        Integer userID = (Integer) request.get("userID");
        String currencyCode = (String) request.get("currencyCode");
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        
        return ResponseEntity.ok(walletService.deposit(userID, currencyCode, amount));
    }
}
