package com.example.smartcurrency.repository;

import com.example.smartcurrency.model.Transaction;
import com.example.smartcurrency.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByUser(User user);

    List<Transaction> findByUserOrderByTransactionDateDesc(User user);

    @Query("SELECT t FROM Transaction t WHERE t.user = :user " +
           "AND (cast(:startDate as timestamp) IS NULL OR t.transactionDate >= :startDate) " +
           "AND (cast(:endDate as timestamp) IS NULL OR t.transactionDate <= :endDate) " +
           "AND (:targetCurrencyCode IS NULL OR t.toCurrency.currencyCode = :targetCurrencyCode) " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findFilteredTransactions(
        @Param("user") User user, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate, 
        @Param("targetCurrencyCode") String targetCurrencyCode
    );

    @Query("SELECT COALESCE(SUM(t.baseAmount), 0) FROM Transaction t WHERE t.user = :user AND t.transactionDate >= :startOfDay AND t.status = 'COMPLETED'")
    BigDecimal sumTodayBaseAmount(@Param("user") User user, @Param("startOfDay") LocalDateTime startOfDay);
}
