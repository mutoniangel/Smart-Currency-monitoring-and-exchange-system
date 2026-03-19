package com.example.smartcurrency.service;

import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CaptchaService {
    private final Random random = new Random();
    // In a real app, use a proper session or cache. Here we use a simple map for demo.
    private final ConcurrentHashMap<String, String> capchas = new ConcurrentHashMap<>();

    public String generateCaptcha(String sessionId) {
        int a = random.nextInt(10) + 1;
        int b = random.nextInt(10) + 1;
        String question = a + " + " + b;
        capchas.put(sessionId, String.valueOf(a + b));
        return question;
    }

    public boolean verifyCaptcha(String sessionId, String answer) {
        String correct = capchas.remove(sessionId);
        return correct != null && correct.equals(answer);
    }
}
