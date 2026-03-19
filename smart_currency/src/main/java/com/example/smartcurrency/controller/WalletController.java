package com.example.smartcurrency.controller;

import com.example.smartcurrency.model.Wallet;
import com.example.smartcurrency.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}
