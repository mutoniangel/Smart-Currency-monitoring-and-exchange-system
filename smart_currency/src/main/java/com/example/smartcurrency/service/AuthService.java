package com.example.smartcurrency.service;

import com.example.smartcurrency.dto.LoginRequest;
import com.example.smartcurrency.dto.RegisterRequest;
import com.example.smartcurrency.model.User;
import com.example.smartcurrency.model.Wallet;
import com.example.smartcurrency.repository.UserRepository;
import com.example.smartcurrency.repository.CurrencyRepository;
import com.example.smartcurrency.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole("USER");
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        // Initialize default wallets with starting balances
        currencyRepository.findById("USD").ifPresent(currency -> {
            Wallet wallet = new Wallet(savedUser, currency, new BigDecimal("1000.00"));
            walletRepository.save(wallet);
        });
        currencyRepository.findById("RWF").ifPresent(currency -> {
            Wallet wallet = new Wallet(savedUser, currency, new BigDecimal("50000.00"));
            walletRepository.save(wallet);
        });

        return savedUser;
    }

    public Optional<User> authenticate(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(User::isEnabled)
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()));
    }
}
