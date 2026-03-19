package com.example.smartcurrency.service;

import com.example.smartcurrency.dto.TransactionRequest;
import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.model.Transaction;
import com.example.smartcurrency.model.User;
import com.example.smartcurrency.model.Wallet;
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
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Transaction performExchange(String username, TransactionRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Currency fromCurr = currencyRepository.findById(request.getFromCurrency())
                .orElseThrow(() -> new RuntimeException("Source currency not found"));
        Currency toCurr = currencyRepository.findById(request.getToCurrency())
                .orElseThrow(() -> new RuntimeException("Target currency not found"));

        if (fromCurr.getCurrencyCode().equals(toCurr.getCurrencyCode())) {
            throw new RuntimeException("Cannot exchange the same currency");
        }

        Wallet fromWallet = walletRepository.findByUserAndCurrency(user, fromCurr)
                .orElseThrow(() -> new RuntimeException("You don't have a " + fromCurr.getCurrencyCode() + " wallet"));
        
        if (fromWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Calculate exchange rate
        BigDecimal rate = toCurr.getCurrentRate().divide(fromCurr.getCurrentRate(), 4, RoundingMode.HALF_UP);
        
        // Fee calculation (1%)
        BigDecimal fee = request.getAmount().multiply(new BigDecimal("0.01"));
        BigDecimal sourceAmountAfterFee = request.getAmount().subtract(fee);
        BigDecimal targetAmount = sourceAmountAfterFee.multiply(rate);

        // Update wallets
        fromWallet.setBalance(fromWallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(fromWallet);

        Wallet toWallet = walletRepository.findByUserAndCurrency(user, toCurr)
                .orElseGet(() -> new Wallet(user, toCurr, BigDecimal.ZERO));
        toWallet.setBalance(toWallet.getBalance().add(targetAmount));
        walletRepository.save(toWallet);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setFromCurrency(fromCurr);
        transaction.setToCurrency(toCurr);
        transaction.setAmount(request.getAmount());
        transaction.setExchangeRate(rate);
        transaction.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findByUser(user);
    }
}
