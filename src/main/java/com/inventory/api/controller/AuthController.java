package com.inventory.api.controller;

import com.inventory.api.dto.ApiResponse;
import com.inventory.api.dto.AuthResponse;
import com.inventory.api.dto.LoginRequest;
import com.inventory.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse authResponse = authService.login(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .data(authResponse)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message("Login failed: " + e.getMessage())
                    .build());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody com.inventory.api.dto.RegisterRequest request) {
        try {
            AuthResponse authResponse = authService.register(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Registration successful")
                    .data(authResponse)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message("Registration failed: " + e.getMessage())
                    .build());
        }
    }
}
