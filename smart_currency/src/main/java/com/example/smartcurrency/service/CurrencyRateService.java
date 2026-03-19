package com.example.smartcurrency.service;

import com.example.smartcurrency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyRateService {

    @Autowired
    private CurrencyRepository currencyRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";

    @Scheduled(fixedRate = 3600000)
    @SuppressWarnings("unchecked")
    public void refreshRates() {
        try {
            Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);
            if (response != null && response.containsKey("rates")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                
                currencyRepository.findAll().forEach(curr -> {
                    String code = curr.getCurrencyCode();
                    if (rates.containsKey(code)) {
                        Object rateObj = rates.get(code);
                        double rateVal = 0.0;
                        if (rateObj instanceof Number) {
                            rateVal = ((Number) rateObj).doubleValue();
                        }
                        
                        BigDecimal newRate = BigDecimal.valueOf(rateVal);
                        if (newRate.compareTo(curr.getCurrentRate()) > 0) curr.setTrend("UP");
                        else if (newRate.compareTo(curr.getCurrentRate()) < 0) curr.setTrend("DOWN");
                        else curr.setTrend("STABLE");
                        curr.setCurrentRate(newRate);
                        currencyRepository.save(curr);
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch live rates: " + e.getMessage());
        }
    }
}
