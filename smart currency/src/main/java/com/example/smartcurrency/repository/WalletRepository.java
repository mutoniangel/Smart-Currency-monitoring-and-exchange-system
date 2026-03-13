package com.example.smartcurrency.repository;

import com.example.smartcurrency.model.Wallet;
import com.example.smartcurrency.model.User;
import com.example.smartcurrency.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    List<Wallet> findByUserUserID(Integer userID);
    Optional<Wallet> findByUserAndCurrency(User user, Currency currency);
}
