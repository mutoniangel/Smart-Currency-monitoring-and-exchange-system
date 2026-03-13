package com.example.smartcurrency.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Currency")
public class Currency {
    @Id
    @Column(length = 10)
    private String currencyCode;

    @Column(nullable = false, length = 50)
    private String currencyName;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal currentRate;

    @Column(length = 20)
    private String trend;

    @ManyToOne
    @JoinColumn(name = "AdminID")
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
