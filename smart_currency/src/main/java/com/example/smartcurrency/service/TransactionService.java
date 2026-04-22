package com.example.smartcurrency.service;

import com.example.smartcurrency.dto.TransactionRequest;
import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.model.Transaction;
import com.example.smartcurrency.model.User;
import com.example.smartcurrency.model.Wallet;
import com.example.smartcurrency.model.TransactionStatus;
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

        // Calculate base amount (USD equivalent) for daily limit tracking
        BigDecimal baseEquivalent = request.getAmount().divide(fromCurr.getCurrentRate(), 4, RoundingMode.HALF_UP);

        // Daily Limit Check (10,000 USD equivalent per day)
        BigDecimal maxDailyLimit = new BigDecimal("10000");
        BigDecimal todayTotal = transactionRepository.sumTodayBaseAmount(user, LocalDateTime.now().toLocalDate().atStartOfDay());

        if (todayTotal.add(baseEquivalent).compareTo(maxDailyLimit) > 0) {
            // Save a FAILED transaction record for audit trail
            Transaction failedTx = new Transaction();
            failedTx.setUser(user);
            failedTx.setFromCurrency(fromCurr);
            failedTx.setToCurrency(toCurr);
            failedTx.setAmount(request.getAmount());
            failedTx.setBaseAmount(baseEquivalent);
            failedTx.setExchangeRate(BigDecimal.ZERO);
            failedTx.setFee(BigDecimal.ZERO);
            failedTx.setConvertedAmount(BigDecimal.ZERO);
            failedTx.setTransactionDate(LocalDateTime.now());
            failedTx.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(failedTx);

            throw new RuntimeException("Daily transaction volume limit exceeded (Max 10,000 USD equivalent per day). Today's total: " + todayTotal.setScale(2, RoundingMode.HALF_UP) + " USD");
        }

        // Check source wallet exists and has sufficient balance
        Wallet fromWallet = walletRepository.findByUserAndCurrency(user, fromCurr)
                .orElseThrow(() -> new RuntimeException("You don't have a " + fromCurr.getCurrencyCode() + " wallet"));

        if (fromWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance. Available: " + fromWallet.getBalance().setScale(2, RoundingMode.HALF_UP) + " " + fromCurr.getCurrencyCode());
        }

        // Calculate exchange rate: toCurrency / fromCurrency
        BigDecimal rate = toCurr.getCurrentRate().divide(fromCurr.getCurrentRate(), 6, RoundingMode.HALF_UP);

        // Fee calculation (1%)
        BigDecimal fee = request.getAmount().multiply(new BigDecimal("0.01")).setScale(4, RoundingMode.HALF_UP);
        BigDecimal sourceAmountAfterFee = request.getAmount().subtract(fee);
        BigDecimal convertedAmount = sourceAmountAfterFee.multiply(rate).setScale(4, RoundingMode.HALF_UP);

        // Update source wallet (deduct full amount including fee)
        fromWallet.setBalance(fromWallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(fromWallet);

        // Update or create target wallet (add converted amount)
        Wallet toWallet = walletRepository.findByUserAndCurrency(user, toCurr)
                .orElseGet(() -> new Wallet(user, toCurr, BigDecimal.ZERO));
        toWallet.setBalance(toWallet.getBalance().add(convertedAmount));
        walletRepository.save(toWallet);

        // Create transaction record with all computed fields
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setFromCurrency(fromCurr);
        transaction.setToCurrency(toCurr);
        transaction.setAmount(request.getAmount());
        transaction.setExchangeRate(rate);
        transaction.setFee(fee);
        transaction.setConvertedAmount(convertedAmount);
        transaction.setBaseAmount(baseEquivalent);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getFilteredTransactions(String username, LocalDateTime startDate, LocalDateTime endDate, String targetCurrencyCode) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findFilteredTransactions(user, startDate, endDate, targetCurrencyCode);
    }

    public List<Transaction> getTransactionsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findByUserOrderByTransactionDateDesc(user);
    }
}
