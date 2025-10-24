// service/EmailService.java
package com.tutorai.tutoraibe.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${sendgrid.api-key}") private String apiKey;
    @Value("${mail.from.address}") private String fromAddress;
    @Value("${mail.from.name}") private String fromName;
    @Value("${app.base-url}") private String appBaseUrl; // để tạo link verify/reset

    public void sendVerificationEmail(String to, String token) {
        String verifyLink = appBaseUrl + "/auth/verify?token=" + token;
        String subject = "Verify your TutorAI account";
        String html = """
      <h2>Welcome to TutorAI</h2>
      <p>Please verify your email by clicking the link below:</p>
      <p><a href="%s">Verify my account</a></p>
      <p>This link expires in 24 hours.</p>
    """.formatted(verifyLink);
        send(to, subject, html);
    }

    public void sendResetPasswordEmail(String to, String token) {
        String link = appBaseUrl + "/auth/reset?token=" + token; // FE có thể nhận token từ query
        String subject = "Reset your TutorAI password";
        String html = """
      <h2>Password Reset</h2>
      <p>Click the link below to set a new password:</p>
      <p><a href="%s">Reset password</a></p>
      <p>Link expires in 30 minutes.</p>
    """.formatted(link);
        send(to, subject, html);
    }

    private void send(String to, String subject, String html){
        try{
            Mail mail = new Mail(new Email(fromAddress, fromName), subject, new Email(to), new Content("text/html", html));
            Request req = new Request();
            req.setMethod(Method.POST);
            req.setEndpoint("mail/send");
            req.setBody(mail.build());
            Response res = new SendGrid(apiKey).api(req);
            if (res.getStatusCode() >= 400) throw new RuntimeException("SendGrid error " + res.getStatusCode());
        }catch (Exception e){
            throw new RuntimeException("Email send failed", e);
        }
    }
}
