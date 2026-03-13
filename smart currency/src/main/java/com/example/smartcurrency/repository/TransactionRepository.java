package com.example.smartcurrency.repository;

import com.example.smartcurrency.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByUserUserID(Integer userID);
}
