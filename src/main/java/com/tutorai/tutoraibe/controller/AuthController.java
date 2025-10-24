package com.tutorai.tutoraibe.controller;

import com.tutorai.tutoraibe.dto.Request.*;
import com.tutorai.tutoraibe.dto.Response.AuthResponse;
import com.tutorai.tutoraibe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("/auth") @RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req){
        return ResponseEntity.ok(Map.of("message", authService.register(req)));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token){
        return ResponseEntity.ok(Map.of("message", authService.verify(token)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req){
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshTokenRequest req){
        return ResponseEntity.ok(authService.refresh(req));
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody @Valid ForgotPasswordRequest req){
        return ResponseEntity.ok(Map.of("message", authService.forgotPassword(req)));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody @Valid ResetPasswordRequest req){
        return ResponseEntity.ok(Map.of("message", authService.resetPassword(req)));
    }
}
