package com.tutorai.tutoraibe.config;

import com.tutorai.tutoraibe.dto.Response.AuthResponse;
import com.tutorai.tutoraibe.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j @Component @RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User ou = (OAuth2User) authentication.getPrincipal();
        Map<String,Object> a = ou.getAttributes();
        String email = str(a.get("email"));
        String name  = strOr(a.get("name"), email);
        String pic   = str(a.get("picture"));

        if (email == null || email.isBlank()) {
            redirect(response, "error=missing_email"); return;
        }
        AuthResponse ar = authService.loginWithGoogleProfile(email, name, pic);
        redirect(response, "token=" + URLEncoder.encode(ar.getAccessToken(), StandardCharsets.UTF_8));
    }

    private void redirect(HttpServletResponse res, String q) throws IOException {
        String ori = System.getenv().getOrDefault("FRONTEND_ORIGINS","http://localhost:5173").split(",")[0].trim();
        if (!ori.startsWith("http")) ori = "http://" + ori;
        getRedirectStrategy().sendRedirect(null, res, ori + "/auth/callback?" + q);
    }
    private String str(Object v){ return v==null?null:String.valueOf(v); }
    private String strOr(Object v,String f){ String s=str(v); return (s==null||s.isBlank())?f:s; }
}
