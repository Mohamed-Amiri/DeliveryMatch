package com.deliverymatch.service;

import com.deliverymatch.dto.AuthenticationRequest;
import com.deliverymatch.dto.AuthenticationResponse;
import com.deliverymatch.dto.RegisterRequest;
import com.deliverymatch.entity.User;
import com.deliverymatch.repository.UserRepository;
import com.deliverymatch.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        logger.debug("Registering new user with email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Set<String> roles = new HashSet<>();
        roles.add(request.getRole());

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .active(true)
                .verified(true)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        logger.debug("User registered successfully: {}", user.getEmail());
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(request.getRole())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        logger.debug("Authenticating user with email: {}", request.getEmail());
        
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            logger.debug("Authentication successful for user: {}", request.getEmail());

            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            var jwtToken = jwtService.generateToken(user);
            logger.debug("JWT token generated for user: {}", user.getEmail());
            
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .email(user.getEmail())
                    .role(user.getRoles().iterator().next())
                    .build();
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", request.getEmail(), e);
            throw e;
        }
    }
} 