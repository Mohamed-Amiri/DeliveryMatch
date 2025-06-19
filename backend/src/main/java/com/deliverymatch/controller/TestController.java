package com.deliverymatch.controller;

import com.deliverymatch.entity.User;
import com.deliverymatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {
    private final UserRepository userRepository;

    @GetMapping("/auth-status")
    public ResponseEntity<Map<String, Object>> getAuthStatus() {
        Map<String, Object> response = new HashMap<>();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        response.put("authenticated", authentication != null && authentication.isAuthenticated());
        response.put("principal", authentication != null ? authentication.getPrincipal() : "null");
        response.put("authorities", authentication != null ? authentication.getAuthorities() : "null");
        
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            String email = (String) authentication.getPrincipal();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                response.put("userActive", user.isActive());
                response.put("userVerified", user.isVerified());
                response.put("userRoles", user.getRoles());
            }
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Map<String, Object>> getUserInfo(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("active", user.isActive());
            response.put("verified", user.isVerified());
            response.put("roles", user.getRoles());
            response.put("enabled", user.isEnabled());
        } else {
            response.put("error", "User not found");
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public String test() {
        return "Backend is working!";
    }
} 