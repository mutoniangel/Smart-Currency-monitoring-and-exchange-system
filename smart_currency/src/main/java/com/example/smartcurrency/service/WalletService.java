package com.example.smartcurrency.service;

import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.model.User;
import com.example.smartcurrency.model.Wallet;
import com.example.smartcurrency.repository.CurrencyRepository;
import com.example.smartcurrency.repository.UserRepository;
import com.example.smartcurrency.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Wallet> getWalletsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return walletRepository.findByUser(user);
    }

    @Transactional
    public Wallet deposit(String username, String currencyCode, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Currency currency = currencyRepository.findById(currencyCode)
                .orElseThrow(() -> new RuntimeException("Currency not found: " + currencyCode));

        Wallet wallet = walletRepository.findByUserAndCurrency(user, currency)
                .orElseGet(() -> new Wallet(user, currency, BigDecimal.ZERO));

        wallet.setBalance(wallet.getBalance().add(amount));
        return walletRepository.save(wallet);
    }

    @Transactional
    public Wallet withdraw(String username, String currencyCode, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than zero");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Currency currency = currencyRepository.findById(currencyCode)
                .orElseThrow(() -> new RuntimeException("Currency not found: " + currencyCode));

        Wallet wallet = walletRepository.findByUserAndCurrency(user, currency)
                .orElseThrow(() -> new RuntimeException("No wallet found for " + currencyCode));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance. Available: " + wallet.getBalance());
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        return walletRepository.save(wallet);
    }
}
