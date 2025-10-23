package com.tutorai.tutoraibe.controller;

import com.tutorai.tutoraibe.dto.Request.*;
import com.tutorai.tutoraibe.dto.Response.AuthResponse;
import com.tutorai.tutoraibe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Đăng ký tài khoản mới
    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    // Xác minh email khi người dùng nhấn vào link verify
    @GetMapping("/verify")
    public String verify(@RequestParam String token) {
        return authService.verify(token);
    }

    // Đăng nhập -> trả về accessToken + refreshToken
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    // Làm mới accessToken khi hết hạn
    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest req) {
        return authService.refresh(req);
    }

    // Gửi email đặt lại mật khẩu
    @PostMapping("/forgot-password")
    public String forgot(@Valid @RequestBody ForgotPasswordRequest req) {
        return authService.forgotPassword(req);
    }

    // Đặt lại mật khẩu mới bằng token gửi qua email
    @PostMapping("/reset-password")
    public String reset(@Valid @RequestBody ResetPasswordRequest req) {
        return authService.resetPassword(req);
    }
}
