package com.example.smartcurrency.controller;

import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<Currency>> getCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Currency>> filterCurrencies(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String trend
    ) {
        return ResponseEntity.ok(currencyService.filterCurrencies(code, trend));
    }
}
