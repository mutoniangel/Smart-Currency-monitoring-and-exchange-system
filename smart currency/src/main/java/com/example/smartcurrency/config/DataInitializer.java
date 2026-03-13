package com.example.smartcurrency.config;

import com.example.smartcurrency.model.Admin;
import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.repository.AdminRepository;
import com.example.smartcurrency.repository.CurrencyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(AdminRepository adminRepository, CurrencyRepository currencyRepository) {
        return args -> {
            if (adminRepository.count() == 0) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword("admin123");
                admin.setEmail("admin@smartcurrency.com");
                admin = adminRepository.save(admin);

                if (currencyRepository.count() == 0) {
                    Currency usd = new Currency();
                    usd.setCurrencyCode("USD");
                    usd.setCurrencyName("US Dollar");
                    usd.setCurrentRate(new BigDecimal("1.0000"));
                    usd.setTrend("Stable");
                    usd.setAdmin(admin);
                    currencyRepository.save(usd);

                    Currency eur = new Currency();
                    eur.setCurrencyCode("EUR");
                    eur.setCurrencyName("Euro");
                    eur.setCurrentRate(new BigDecimal("0.9200"));
                    eur.setTrend("Up");
                    eur.setAdmin(admin);
                    currencyRepository.save(eur);
                }
            }
        };
    }
}
