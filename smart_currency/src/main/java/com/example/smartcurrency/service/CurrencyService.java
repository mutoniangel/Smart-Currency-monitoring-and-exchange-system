package com.example.smartcurrency.service;

import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public List<Currency> filterCurrencies(String code, String trend) {
        return currencyRepository.findFilteredCurrencies(code, trend);
    }

    public void seedCurrencies() {
        ensureCurrency("USD", "US Dollar", new java.math.BigDecimal("1.0000"));
        ensureCurrency("EUR", "Euro", new java.math.BigDecimal("0.9200"));
        ensureCurrency("GBP", "British Pound", new java.math.BigDecimal("0.7800"));
        ensureCurrency("JPY", "Japanese Yen", new java.math.BigDecimal("150.2500"));
        ensureCurrency("UGX", "Uganda Shillings", new java.math.BigDecimal("3800.0000"));
        ensureCurrency("KES", "Kenyan Shillings", new java.math.BigDecimal("130.0000"));
        ensureCurrency("TZS", "Tanzanian Shillings", new java.math.BigDecimal("2500.0000"));
        ensureCurrency("RWF", "Rwandan Franc", new java.math.BigDecimal("1200.0000"));
        ensureCurrency("CAD", "Canadian Dollar", new java.math.BigDecimal("1.3500"));
        ensureCurrency("AUD", "Australian Dollar", new java.math.BigDecimal("1.5200"));
        ensureCurrency("CHF", "Swiss Franc", new java.math.BigDecimal("0.8800"));
        ensureCurrency("CNY", "Chinese Yuan", new java.math.BigDecimal("7.1900"));
        ensureCurrency("INR", "Indian Rupee", new java.math.BigDecimal("82.9000"));
        ensureCurrency("ZAR", "South African Rand", new java.math.BigDecimal("18.9500"));
        ensureCurrency("AED", "UAE Dirham", new java.math.BigDecimal("3.6700"));
        ensureCurrency("SAR", "Saudi Riyal", new java.math.BigDecimal("3.7500"));
        ensureCurrency("TRY", "Turkish Lira", new java.math.BigDecimal("32.1000"));
        ensureCurrency("BRL", "Brazilian Real", new java.math.BigDecimal("4.9800"));
        ensureCurrency("MXN", "Mexican Peso", new java.math.BigDecimal("16.8000"));
        ensureCurrency("NGN", "Nigerian Naira", new java.math.BigDecimal("1550.0000"));
    }

    private void ensureCurrency(String code, String name, java.math.BigDecimal initialRate) {
        if (currencyRepository.findById(code).isEmpty()) {
            Currency c = new Currency();
            c.setCurrencyCode(code);
            c.setCurrencyName(name);
            c.setCurrentRate(initialRate);
            c.setTrend("STABLE");
            currencyRepository.save(c);
        }
    }
}
