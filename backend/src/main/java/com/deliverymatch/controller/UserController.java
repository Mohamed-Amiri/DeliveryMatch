package com.deliverymatch.controller;

import com.deliverymatch.dto.ChangePasswordRequest;
import com.deliverymatch.dto.UserProfileRequest;
import com.deliverymatch.dto.UserProfileResponse;
import com.deliverymatch.repository.UserRepository;
import com.deliverymatch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("User controller is working!");
    }

    @GetMapping("/profile-simple")
    public ResponseEntity<String> getProfileSimple() {
        return ResponseEntity.ok("Profile endpoint is working!");
    }

    @GetMapping("/debug-auth")
    public ResponseEntity<String> debugAuth() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok("Authenticated as: " + email);
        } catch (Exception e) {
            return ResponseEntity.ok("Not authenticated: " + e.getMessage());
        }
    }

    @GetMapping("/debug-users")
    public ResponseEntity<String> debugUsers() {
        try {
            long userCount = userRepository.count();
            return ResponseEntity.ok("Total users in database: " + userCount);
        } catch (Exception e) {
            return ResponseEntity.ok("Error checking users: " + e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateUserProfile(@RequestBody UserProfileRequest request) {
        return ResponseEntity.ok(userService.updateUserProfile(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/deactivate")
    public ResponseEntity<String> deactivateAccount() {
        userService.deactivateAccount();
        return ResponseEntity.ok("Account deactivated successfully");
    }

    @PostMapping("/reactivate")
    public ResponseEntity<String> reactivateAccount() {
        userService.reactivateAccount();
        return ResponseEntity.ok("Account reactivated successfully");
    }
} 