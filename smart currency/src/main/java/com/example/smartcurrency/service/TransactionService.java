package com.example.smartcurrency.service;

import com.example.smartcurrency.model.Transaction;
import com.example.smartcurrency.repository.TransactionRepository;
import com.example.smartcurrency.repository.CurrencyRepository;
import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.model.User;
import com.example.smartcurrency.model.Wallet;
import com.example.smartcurrency.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private WalletRepository walletRepository;

    @jakarta.transaction.Transactional
    public Transaction processTransaction(Transaction transaction) {
        User user = transaction.getUser();
        Currency from = currencyRepository.findById(transaction.getFromCurrency().getCurrencyCode())
                .orElseThrow(() -> new RuntimeException("Source currency not found"));
        Currency to = currencyRepository.findById(transaction.getToCurrency().getCurrencyCode())
                .orElseThrow(() -> new RuntimeException("Target currency not found"));

        // Get source wallet and check balance
        Wallet sourceWallet = walletRepository.findByUserAndCurrency(user, from)
                .orElseThrow(() -> new RuntimeException("Source wallet not found"));

        if (sourceWallet.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new com.example.smartcurrency.exception.InsufficientFundsException("Not enough balance in " + from.getCurrencyCode());
        }

        // Calculate rate and target amount
        BigDecimal rate = to.getCurrentRate().divide(from.getCurrentRate(), 4, RoundingMode.HALF_UP);
        BigDecimal targetAmount = transaction.getAmount().multiply(rate);
        
        transaction.setExchangeRate(rate);

        // Update wallets
        sourceWallet.setBalance(sourceWallet.getBalance().subtract(transaction.getAmount()));
        walletRepository.save(sourceWallet);

        Wallet targetWallet = walletRepository.findByUserAndCurrency(user, to)
                .orElseGet(() -> {
                    Wallet w = new Wallet(user, to, BigDecimal.ZERO);
                    return walletRepository.save(w);
                });
        
        targetWallet.setBalance(targetWallet.getBalance().add(targetAmount));
        walletRepository.save(targetWallet);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getUserTransactions(Integer userID) {
        return transactionRepository.findByUserUserID(userID);
    }
}
