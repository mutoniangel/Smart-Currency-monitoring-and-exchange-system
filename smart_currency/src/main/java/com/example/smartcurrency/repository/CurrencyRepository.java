package com.example.smartcurrency.repository;

import com.example.smartcurrency.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {
    @Query("SELECT c FROM Currency c WHERE " +
           "(:code IS NULL OR LOWER(c.currencyCode) LIKE LOWER(CONCAT('%', :code, '%'))) AND " +
           "(:trend IS NULL OR LOWER(c.trend) = LOWER(:trend))")
    List<Currency> findFilteredCurrencies(@Param("code") String code, @Param("trend") String trend);
}
