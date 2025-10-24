package com.tutorai.tutoraibe.service;

import com.tutorai.tutoraibe.dto.Request.*;
import com.tutorai.tutoraibe.dto.Response.AuthResponse;
import com.tutorai.tutoraibe.entity.*;
import com.tutorai.tutoraibe.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Service @RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final VerificationTokenRepository verifyRepo;
    private final PasswordResetTokenRepository resetRepo;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    /* REGISTER + SEND VERIFY LINK */
    @Transactional
    public String register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) return "Email already exists";

        var role = Optional.ofNullable(req.getRole()).orElse("STUDENT").toUpperCase();
        User user = User.builder()
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .role(User.Role.valueOf(role))
                .status(User.Status.INACTIVE)
                .emailVerified(false)
                .build();
        userRepo.save(user);

        String token = UUID.randomUUID().toString();
        verifyRepo.save(VerificationToken.builder()
                .token(token).user(user)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build());

        emailService.sendVerificationEmail(user.getEmail(), token);
        return "Registered! Please check your email to verify your account.";
    }

    /* VERIFY EMAIL */
    @Transactional
    public String verify(String token) {
        var v = verifyRepo.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid token"));
        if (v.getExpiryDate().isBefore(LocalDateTime.now())) return "Token expired";
        var u = v.getUser();
        u.setStatus(User.Status.ACTIVE);
        u.setEmailVerified(true);
        userRepo.save(u);
        verifyRepo.delete(v);
        return "Account verified successfully!";
    }

    /* LOGIN (password) */
    public AuthResponse login(LoginRequest req) {
        var user = userRepo.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("Email not found"));
        if (user.getStatus() != User.Status.ACTIVE) throw new RuntimeException("Account not verified/active");
        if (!encoder.matches(req.getPassword(), user.getPassword())) throw new RuntimeException("Invalid credentials");

        return issueTokens(user);
    }

    /* REFRESH */
    public AuthResponse refresh(RefreshTokenRequest req) {
        String email = jwtService.extractEmail(req.getRefreshToken());
        var user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (!jwtService.isValid(req.getRefreshToken(), email)) throw new RuntimeException("Invalid refresh token");

        String access = jwtService.generateAccessToken(email, Map.of("role", user.getRole().name(), "uid", user.getId()));
        return AuthResponse.builder().accessToken(access).refreshToken(req.getRefreshToken()).message("Refreshed").build();
    }

    /* FORGOT PASSWORD → SEND MAIL */
    @Transactional
    public String forgotPassword(ForgotPasswordRequest req) {
        var userOpt = userRepo.findByEmail(req.getEmail());
        if (userOpt.isEmpty()) return "If this email exists, a reset link has been sent.";
        var user = userOpt.get();

        String token = UUID.randomUUID().toString();
        resetRepo.save(PasswordResetToken.builder()
                .token(token).user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .used(false).build());

        emailService.sendResetPasswordEmail(user.getEmail(), token);
        return "If this email exists, a reset link has been sent.";
    }

    /* RESET PASSWORD */
    @Transactional
    public String resetPassword(ResetPasswordRequest req) {
        var prt = resetRepo.findByToken(req.getToken()).orElseThrow(() -> new RuntimeException("Invalid reset token"));
        if (prt.isUsed()) return "Token already used";
        if (prt.getExpiryDate().isBefore(LocalDateTime.now())) return "Token expired";

        var user = prt.getUser();
        user.setPassword(encoder.encode(req.getNewPassword()));
        userRepo.save(user);

        prt.setUsed(true);
        resetRepo.save(prt);
        return "Password reset successfully";
    }

    /* GOOGLE OAUTH2: upsert + JWT */
    @Transactional
    public AuthResponse loginWithGoogleProfile(String email, String fullName, String avatarUrl) {
        var user = userRepo.findByEmail(email).map(u -> {
            if (fullName != null && !fullName.isBlank()) u.setFullName(fullName);
            if (avatarUrl != null && !avatarUrl.isBlank()) u.setAvatarUrl(avatarUrl);
            if (u.getStatus() == User.Status.INACTIVE) u.setStatus(User.Status.ACTIVE);
            if (u.getEmailVerified() == null || !u.getEmailVerified()) u.setEmailVerified(true);
            return userRepo.save(u);
        }).orElseGet(() -> {
            var u = User.builder()
                    .email(email)
                    .fullName((fullName == null || fullName.isBlank()) ? email : fullName)
                    .avatarUrl(avatarUrl)
                    .role(User.Role.STUDENT)
                    .status(User.Status.ACTIVE)
                    .emailVerified(true)
                    .password(encoder.encode("oauth2-" + randomSecret(24)))
                    .build();
            return userRepo.save(u);
        });

        return issueTokens(user);
    }

    /* Helpers */
    private AuthResponse issueTokens(User user) {
        Map<String,Object> claims = Map.of("role", user.getRole().name(), "uid", user.getId());
        String access = jwtService.generateAccessToken(user.getEmail(), claims);
        String refresh = jwtService.generateRefreshToken(user.getEmail());
        return AuthResponse.builder().accessToken(access).refreshToken(refresh).message("Login successful").build();
    }

    private String randomSecret(int bytes) {
        byte[] buf = new byte[bytes];
        new SecureRandom().nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }
}
