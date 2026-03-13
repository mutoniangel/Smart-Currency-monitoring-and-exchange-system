package com.example.smartcurrency.repository;

import com.example.smartcurrency.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, String> {
}
