package com.example.smartcurrency.dto;

import java.math.BigDecimal;

public class TransactionRequest {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;

    public TransactionRequest() {}

    public String getFromCurrency() { return fromCurrency; }
    public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }

    public String getToCurrency() { return toCurrency; }
    public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
