package com.example.smartcurrency.controller;

import com.example.smartcurrency.dto.AuthResponse;
import com.example.smartcurrency.dto.LoginRequest;
import com.example.smartcurrency.dto.RegisterRequest;
import com.example.smartcurrency.model.User;
import com.example.smartcurrency.repository.UserRepository;
import com.example.smartcurrency.security.JwtUtil;
import com.example.smartcurrency.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private com.example.smartcurrency.service.CaptchaService captchaService;

    @GetMapping("/captcha")
    public ResponseEntity<String> getCaptcha(@RequestParam String sessionId) {
        return ResponseEntity.ok(captchaService.generateCaptcha(sessionId));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String sessionId, @Valid @RequestBody RegisterRequest request) {
        if (!captchaService.verifyCaptcha(sessionId, request.getCaptchaAnswer())) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "Invalid CAPTCHA answer"));
        }
        authService.registerUser(request);
        return ResponseEntity.ok("User registered successfully. You can now login.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String sessionId, @Valid @RequestBody LoginRequest request) {
        if (!captchaService.verifyCaptcha(sessionId, request.getCaptchaAnswer())) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "Invalid CAPTCHA answer"));
        }
        return authService.authenticate(request)
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getUsername(), "ROLE_" + user.getRole().toUpperCase());
                    return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole()));
                })
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .map(user -> {
                    user.setPassword(null);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody User updateRequest) {
        return userRepository.findByUsername(authentication.getName())
                .map(user -> {
                    user.setEmail(updateRequest.getEmail());
                    if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
                        user.setPassword(new BCryptPasswordEncoder().encode(updateRequest.getPassword()));
                    }
                    userRepository.save(user);
                    return ResponseEntity.ok("Profile updated");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
