package com.example.smartcurrency.controller;

import com.example.smartcurrency.dto.TransactionRequest;
import com.example.smartcurrency.model.Transaction;
import com.example.smartcurrency.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/filter")
    public ResponseEntity<?> filterTransactions(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String targetCurrency
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(transactionService.getFilteredTransactions(username, startDate, endDate, targetCurrency));
    }

    @PostMapping("/exchange")
    public ResponseEntity<?> exchange(Authentication authentication, @Valid @RequestBody TransactionRequest request) {
        String username = authentication.getName();
        Transaction transaction = transactionService.performExchange(username, request);
        return ResponseEntity.ok(Map.of(
            "message", "Exchange completed successfully",
            "transactionID", transaction.getTransactionID(),
            "from", transaction.getFromCurrency().getCurrencyCode(),
            "to", transaction.getToCurrency().getCurrencyCode(),
            "amountSold", transaction.getAmount(),
            "fee", transaction.getFee(),
            "exchangeRate", transaction.getExchangeRate(),
            "convertedAmount", transaction.getConvertedAmount(),
            "status", transaction.getStatus().name()
        ));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyTransactions(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(transactionService.getTransactionsByUser(username));
    }
}
