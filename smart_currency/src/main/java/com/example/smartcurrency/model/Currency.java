package com.example.smartcurrency.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Currency")
public class Currency {
    @Id
    @Column(name = "currency_code", length = 10)
    private String currencyCode;

    @Column(name = "currency_name", nullable = false, length = 50)
    private String currencyName;

    @Column(name = "current_rate", nullable = false, precision = 10, scale = 4)
    @jakarta.validation.constraints.DecimalMin(value = "0.0001", message = "Exchange rate must be positive")
    private BigDecimal currentRate;

    @Column(name = "trend", length = 20)
    private String trend;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public Currency() {}

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public String getCurrencyName() { return currencyName; }
    public void setCurrencyName(String currencyName) { this.currencyName = currencyName; }

    public BigDecimal getCurrentRate() { return currentRate; }
    public void setCurrentRate(BigDecimal currentRate) { this.currentRate = currentRate; }

    public String getTrend() { return trend; }
    public void setTrend(String trend) { this.trend = trend; }

    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }
}
