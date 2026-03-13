package com.example.smartcurrency.controller;

import com.example.smartcurrency.model.Transaction;
import com.example.smartcurrency.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/send")
    public ResponseEntity<Transaction> sendMoney(@jakarta.validation.Valid @RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.processTransaction(transaction));
    }

    @GetMapping("/user/{userID}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable Integer userID) {
        return ResponseEntity.ok(transactionService.getUserTransactions(userID));
    }
}
