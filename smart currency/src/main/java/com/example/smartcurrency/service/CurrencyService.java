package com.example.smartcurrency.service;

import com.example.smartcurrency.model.Currency;
import com.example.smartcurrency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";

    /**
     * Updates exchange rates from an external API every hour.
     * Initial delay: 5 seconds after startup.
     */
    @Scheduled(fixedRate = 3600000, initialDelay = 5000)
    public void updateRatesFromApi() {
        try {
            Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);
            if (response != null && response.containsKey("rates")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                
                // Update existing currencies in our database
                for (Currency currency : currencyRepository.findAll()) {
                    String code = currency.getCurrencyCode();
                    if (rates.containsKey(code)) {
                        Object rateObj = rates.get(code);
                        BigDecimal newRate;
                        if (rateObj instanceof Number) {
                            newRate = BigDecimal.valueOf(((Number) rateObj).doubleValue());
                        } else {
                            continue;
                        }
                        
                        // Determine trend
                        if (currency.getCurrentRate() != null) {
                            int cmp = newRate.compareTo(currency.getCurrentRate());
                            if (cmp > 0) currency.setTrend("Up");
                            else if (cmp < 0) currency.setTrend("Down");
                            else currency.setTrend("Stable");
                        }
                        
                        currency.setCurrentRate(newRate);
                        currencyRepository.save(currency);
                    }
                }
                System.out.println("Exchange rates updated successfully from " + API_URL);
            }
        } catch (Exception e) {
            System.err.println("Failed to update exchange rates: " + e.getMessage());
        }
    }
}
