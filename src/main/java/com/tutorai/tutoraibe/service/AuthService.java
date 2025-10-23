package com.tutorai.tutoraibe.service;

import com.tutorai.tutoraibe.dto.Request.*;
import com.tutorai.tutoraibe.dto.Response.AuthResponse;
import com.tutorai.tutoraibe.entity.*;
import com.tutorai.tutoraibe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service @RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final StudentRepository studentRepo;
    private final VerificationTokenRepository verifyRepo;
    private final PasswordResetTokenRepository resetRepo;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    // REGISTER
    public String register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) return "Email already exists";

        var role = Optional.ofNullable(req.getRole()).orElse("STUDENT").toUpperCase();
        User user = User.builder()
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .role(User.Role.valueOf(role))
                .status(User.Status.INACTIVE)
                .build();
        userRepo.save(user);

        if (user.getRole() == User.Role.STUDENT) {
            studentRepo.save(com.tutorai.tutoraibe.entity.Student.builder().user(user).build());
        }

        String token = UUID.randomUUID().toString();
        verifyRepo.save(VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build());

        emailService.sendVerificationEmail(user.getEmail(), token);
        return "Registered! Please check your email to verify your account.";
    }

    // VERIFY EMAIL
    public String verify(String token) {
        var v = verifyRepo.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid token"));
        if (v.getExpiryDate().isBefore(LocalDateTime.now())) return "Token expired";
        User u = v.getUser();
        u.setStatus(User.Status.ACTIVE);
        userRepo.save(u);
        verifyRepo.delete(v);
        return "Account verified successfully!";
    }

    // LOGIN
    public AuthResponse login(LoginRequest req) {
        var user = userRepo.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("Email not found"));
        if (user.getStatus() != User.Status.ACTIVE) throw new RuntimeException("Account not verified/active");
        if (!encoder.matches(req.getPassword(), user.getPassword())) throw new RuntimeException("Invalid credentials");

        Map<String,Object> claims = Map.of("role", user.getRole().name(), "uid", user.getId());
        String access = jwtService.generateAccessToken(user.getEmail(), claims);
        String refresh = jwtService.generateRefreshToken(user.getEmail());
        return AuthResponse.builder().accessToken(access).refreshToken(refresh).message("Login successful").build();
    }

    // REFRESH
    public AuthResponse refresh(RefreshTokenRequest req) {
        String email = jwtService.extractEmail(req.getRefreshToken());
        var user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (!jwtService.isValid(req.getRefreshToken(), email)) throw new RuntimeException("Invalid refresh token");

        Map<String,Object> claims = Map.of("role", user.getRole().name(), "uid", user.getId());
        String access = jwtService.generateAccessToken(email, claims);
        return AuthResponse.builder().accessToken(access).refreshToken(req.getRefreshToken()).message("Refreshed").build();
    }

    // FORGOT PASSWORD
    public String forgotPassword(ForgotPasswordRequest req) {
        var userOpt = userRepo.findByEmail(req.getEmail());
        if (userOpt.isEmpty()) return "If this email exists, a reset link has been sent."; // tránh leak
        var user = userOpt.get();

        String token = UUID.randomUUID().toString();
        resetRepo.save(PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .used(false)
                .build());

        emailService.sendResetPasswordEmail(user.getEmail(), token);
        return "If this email exists, a reset link has been sent.";
    }

    // RESET PASSWORD
    public String resetPassword(ResetPasswordRequest req) {
        var prt = resetRepo.findByToken(req.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));
        if (prt.isUsed()) return "Token already used";
        if (prt.getExpiryDate().isBefore(LocalDateTime.now())) return "Token expired";

        var user = prt.getUser();
        user.setPassword(encoder.encode(req.getNewPassword()));
        userRepo.save(user);

        prt.setUsed(true);
        resetRepo.save(prt);
        return "Password reset successfully";
    }
}
