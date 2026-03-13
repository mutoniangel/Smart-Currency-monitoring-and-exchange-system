package com.example.smartcurrency.service;

import com.example.smartcurrency.model.Wallet;
import com.example.smartcurrency.model.User;
import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.repository.WalletRepository;
import com.example.smartcurrency.repository.UserRepository;
import com.example.smartcurrency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<Wallet> getUserWallets(Integer userID) {
        return walletRepository.findByUserUserID(userID);
    }

    public Wallet deposit(Integer userID, String currencyCode, BigDecimal amount) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Currency currency = currencyRepository.findById(currencyCode)
                .orElseThrow(() -> new RuntimeException("Currency not found"));

        Wallet wallet = walletRepository.findByUserAndCurrency(user, currency)
                .orElseGet(() -> new Wallet(user, currency, BigDecimal.ZERO));

        wallet.setBalance(wallet.getBalance().add(amount));
        return walletRepository.save(wallet);
    }
}
