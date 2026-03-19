package com.example.smartcurrency.repository;

import com.example.smartcurrency.model.Transaction;
import com.example.smartcurrency.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByUser(User user);
}
