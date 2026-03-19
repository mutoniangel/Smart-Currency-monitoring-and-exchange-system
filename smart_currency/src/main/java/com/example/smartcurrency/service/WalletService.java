package com.example.smartcurrency.service;

import com.example.smartcurrency.model.User;
import com.example.smartcurrency.model.Wallet;
import com.example.smartcurrency.repository.UserRepository;
import com.example.smartcurrency.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Wallet> getWalletsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return walletRepository.findByUser(user);
    }
}
