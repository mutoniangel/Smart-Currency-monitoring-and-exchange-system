package com.example.smartcurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartCurrencyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartCurrencyApplication.class, args);
    }
}
