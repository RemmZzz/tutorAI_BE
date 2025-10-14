package com.tutorai.tutoraibe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    //  Đọc URL từ biến môi trường Render (APP_BASE_URL)
    @Value("${APP_BASE_URL:https://tutorai-be.onrender.com}")
    private String backendBaseUrl;  // Mặc định là domain Render

    @Value("${FRONTEND_BASE_URL:http://localhost:5173}")
    private String frontendBaseUrl; // FE local (Vite)

    /**
     * Gửi email xác minh đăng ký
     */
    public void sendVerificationEmail(String to, String token) {
        String verifyUrl = backendBaseUrl + "/api/v1/auth/verify?token=" + token;

        String subject = "Verify your TutorAI account";
        String body = """
                Welcome to TutorAI!
                
                Please verify your account by clicking the link below:
                %s
                
                This link will expire in 24 hours.
                """.formatted(verifyUrl);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
    }

    /**
     * Gửi email đặt lại mật khẩu
     */
    public void sendResetPasswordEmail(String to, String token) {
        String resetUrl = frontendBaseUrl + "/reset-password?token=" + token; // Trang FE reset password

        String subject = "Reset your TutorAI password";
        String body = """
                You requested a password reset.
                Click the link below to set a new password:
                %s
                
                This link expires in 30 minutes.
                """.formatted(resetUrl);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
    }
}
