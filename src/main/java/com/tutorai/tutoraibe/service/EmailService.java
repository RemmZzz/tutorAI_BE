package com.tutorai.tutoraibe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) {
        String verifyUrl = "http://localhost:9000/api/v1/auth/verify?token=" + token;
        String subject = "Verify your TutorAI account";
        String body = "Welcome to TutorAI!\n\nPlease verify your account:\n" + verifyUrl + "\n\nThis link expires in 24 hours.";

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
    }

    public void sendResetPasswordEmail(String to, String token) {
        String resetUrl = "http://localhost:5173/reset-password?token=" + token; // FE xử lý form reset
        String subject = "Reset your TutorAI password";
        String body = "You requested a password reset.\nClick the link to set a new password:\n"
                + resetUrl + "\n\nThis link expires in 30 minutes.";

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
    }
}
