package com.example.smartcurrency.service;

import com.example.smartcurrency.model.*;
import com.example.smartcurrency.repository.CurrencyRepository;
import com.example.smartcurrency.repository.TransactionRepository;
import com.example.smartcurrency.repository.UserRepository;
import com.example.smartcurrency.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private TransactionRepository transactionRepository;

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
        Wallet savedWallet = walletRepository.save(wallet);

        // Log the deposit as a transaction
        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setFromCurrency(null); // No source currency for deposit
        tx.setToCurrency(currency);
        tx.setAmount(amount);
        tx.setConvertedAmount(amount);
        tx.setFee(BigDecimal.ZERO);
        tx.setExchangeRate(BigDecimal.ONE);
        tx.setBaseAmount(amount.divide(currency.getCurrentRate(), 4, RoundingMode.HALF_UP));
        tx.setTransactionDate(LocalDateTime.now());
        tx.setStatus(TransactionStatus.COMPLETED);
        tx.setTransactionType(TransactionType.DEPOSIT);
        transactionRepository.save(tx);

        return savedWallet;
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
        Wallet savedWallet = walletRepository.save(wallet);

        // Log the withdrawal as a transaction
        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setFromCurrency(currency);
        tx.setToCurrency(null); // No target currency for withdrawal
        tx.setAmount(amount);
        tx.setConvertedAmount(BigDecimal.ZERO);
        tx.setFee(BigDecimal.ZERO);
        tx.setExchangeRate(BigDecimal.ONE);
        tx.setBaseAmount(amount.divide(currency.getCurrentRate(), 4, RoundingMode.HALF_UP));
        tx.setTransactionDate(LocalDateTime.now());
        tx.setStatus(TransactionStatus.COMPLETED);
        tx.setTransactionType(TransactionType.WITHDRAWAL);
        transactionRepository.save(tx);

        return savedWallet;
    }
}
