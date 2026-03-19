package com.example.smartcurrency.config;

import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.model.User;
import com.example.smartcurrency.model.Wallet;
import com.example.smartcurrency.repository.CurrencyRepository;
import com.example.smartcurrency.repository.UserRepository;
import com.example.smartcurrency.repository.WalletRepository;
import com.example.smartcurrency.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CurrencyRateService currencyRateService;

    @Override
    public void run(String... args) throws Exception {
        // Ensure base currencies exist
        ensureCurrency("USD", "US Dollar", new BigDecimal("1.0000"));
        ensureCurrency("EUR", "Euro", new BigDecimal("0.9200"));
        ensureCurrency("GBP", "British Pound", new BigDecimal("0.7800"));
        ensureCurrency("JPY", "Japanese Yen", new BigDecimal("150.2500"));
        ensureCurrency("UGX", "Uganda Shillings", new BigDecimal("3800.0000"));
        ensureCurrency("KES", "Kenyan Shillings", new BigDecimal("130.0000"));
        ensureCurrency("TZS", "Tanzanian Shillings", new BigDecimal("2500.0000"));
        ensureCurrency("RWF", "Rwandan Franc", new BigDecimal("1200.0000"));
        ensureCurrency("CAD", "Canadian Dollar", new BigDecimal("1.3500"));
        ensureCurrency("AUD", "Australian Dollar", new BigDecimal("1.5200"));
        ensureCurrency("CHF", "Swiss Franc", new BigDecimal("0.8800"));
        ensureCurrency("CNY", "Chinese Yuan", new BigDecimal("7.1900"));
        ensureCurrency("INR", "Indian Rupee", new BigDecimal("82.9000"));
        ensureCurrency("ZAR", "South African Rand", new BigDecimal("18.9500"));
        ensureCurrency("AED", "UAE Dirham", new BigDecimal("3.6700"));
        ensureCurrency("SAR", "Saudi Riyal", new BigDecimal("3.7500"));
        ensureCurrency("TRY", "Turkish Lira", new BigDecimal("32.1000"));
        ensureCurrency("BRL", "Brazilian Real", new BigDecimal("4.9800"));
        ensureCurrency("MXN", "Mexican Peso", new BigDecimal("16.8000"));
        ensureCurrency("NGN", "Nigerian Naira", new BigDecimal("1550.0000"));

        System.out.println("Base currencies verified. Triggering live rate sync...");
        try {
            currencyRateService.refreshRates();
            System.out.println("Live rate sync completed on startup.");
        } catch (Exception e) {
            System.err.println("Failed to sync live rates on startup: " + e.getMessage());
        }

        // Initialize admin user
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@smartcurrency.com");
            admin.setRole("ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("Default admin created: admin / admin123");
        } else {
            // Ensure existing admin is enabled AND has ADMIN role
            userRepository.findByUsername("admin").ifPresent(admin -> {
                boolean changed = false;
                if (!admin.isEnabled()) {
                    admin.setEnabled(true);
                    changed = true;
                }
                if (!"ADMIN".equals(admin.getRole())) {
                    admin.setRole("ADMIN");
                    changed = true;
                }
                if (changed) {
                    userRepository.save(admin);
                    System.out.println("Existing admin account has been updated (Role/Enabled).");
                }
            });
        }

        // Fix missing wallets for existing users
        currencyRepository.findById("USD").ifPresent(usd -> {
            userRepository.findAll().forEach(user -> {
                if (!user.isEnabled()) {
                    user.setEnabled(true);
                    userRepository.save(user);
                }
                if (walletRepository.findByUserAndCurrency(user, usd).isEmpty()) {
                    System.out.println("Initializing USD wallet for user: " + user.getUsername());
                    Wallet wallet = new Wallet(user, usd, new BigDecimal("1000.00"));
                    walletRepository.save(wallet);
                }
            });
        });
    }

    private void ensureCurrency(String code, String name, BigDecimal initialRate) {
        if (currencyRepository.findById(code).isEmpty()) {
            Currency c = new Currency();
            c.setCurrencyCode(code);
            c.setCurrencyName(name);
            c.setCurrentRate(initialRate);
            c.setTrend("STABLE");
            currencyRepository.save(c);
            System.out.println("Initial currency added: " + code);
        }
    }
}
