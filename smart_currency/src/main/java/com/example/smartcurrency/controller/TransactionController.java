package com.example.smartcurrency.controller;

import com.example.smartcurrency.dto.TransactionRequest;
import com.example.smartcurrency.model.Transaction;
import com.example.smartcurrency.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/exchange")
    public ResponseEntity<?> exchange(Authentication authentication, @RequestBody TransactionRequest request) {
        try {
            String username = authentication.getName();
            Transaction transaction = transactionService.performExchange(username, request);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyTransactions(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(transactionService.getTransactionsByUser(username));
    }
}
