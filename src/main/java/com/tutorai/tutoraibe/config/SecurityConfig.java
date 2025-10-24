package com.tutorai.tutoraibe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2AuthenticationSuccessHandler oauth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS dùng CorsConfigurationSource bean; CSRF off cho REST
                .cors(c -> {})
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // ---- Public (không cần JWT) ----
                        .requestMatchers(
                                "/auth/**",                    // register, login, refresh, verify, forgot, reset
                                "/oauth2/authorization/**",    // start Google OAuth
                                "/login/oauth2/**",            // Google callback (mặc định)
                                "/actuator/**",
                                "/swagger-ui/**", "/v3/api-docs/**",
                                "/error",
                                // CORS preflight
                                "/**",                         // nếu muốn chặt hơn: HttpMethod.OPTIONS, "/**"
                                "/tutors/search", "/tutors/*",
                                "/subjects", "/subjects/*",
                                "/courses", "/courses/*", "/courses/*/lessons",
                                "/blogs/**", "/faqs/**"
                        ).permitAll()

                        // ---- Admin ----
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // ---- Mọi thứ còn lại cần JWT ----
                        .anyRequest().authenticated()
                )

                // OAuth2 login (Google): sinh JWT & redirect về FE
                .oauth2Login(oauth -> oauth.successHandler(oauth2SuccessHandler))

                // Cho phép tạo session khi CẦN cho OAuth2; JWT API không dùng session
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                // JWT filter trước UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
