package com.example.smartcurrency.service;

import com.example.smartcurrency.model.User;
import com.example.smartcurrency.model.Admin;
import com.example.smartcurrency.repository.UserRepository;
import com.example.smartcurrency.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    public Optional<User> loginUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password));
    }

    public Optional<Admin> loginAdmin(String username, String password) {
        return adminRepository.findByUsername(username)
                .filter(a -> a.getPassword().equals(password));
    }

    public User registerUser(User user) {
        user.setRole("Customer");
        return userRepository.save(user);
    }
}
