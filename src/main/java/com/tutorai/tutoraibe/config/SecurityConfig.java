package com.tutorai.tutoraibe.config;

import com.tutorai.tutoraibe.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * ===============================================
 *             SECURITY CONFIGURATION
 * Cấu hình bảo mật tổng thể cho toàn bộ hệ thống
 * Phiên bản cập nhật theo danh sách module & endpoints
 * ===============================================
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF vì đây là REST API
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // ========================
                        // 1️ Public Endpoints (không yêu cầu JWT)
                        // ========================
                        .requestMatchers(
                                // Auth
                                "/auth/**",
                                // Tutors (tìm kiếm & xem hồ sơ)
                                "/tutors/search",
                                "/tutors/{id}",
                                // Subjects
                                "/subjects",
                                "/subjects/{id}",
                                // Courses (xem danh sách & chi tiết)
                                "/courses",
                                "/courses/{id}",
                                "/courses/{id}/lessons",
                                // Blogs & FAQs
                                "/blogs/**",
                                "/faqs/**",
                                // Health check & Docs
                                "/actuator/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ========================
                        // 2️ Admin Endpoints (chỉ ADMIN)
                        // ========================
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // ========================
                        // 3️ Authenticated Endpoints (yêu cầu JWT)
                        // ========================
                        .requestMatchers(
                                "/users/**",
                                "/students/**",
                                "/tutors/**",
                                "/courses/**",
                                "/enrollments/**",
                                "/bookings/**",
                                "/schedules/**",
                                "/payments/**",
                                "/transactions/**",
                                "/reviews/**",
                                "/ai/**",
                                "/notifications/**",
                                "/wishlist/**",
                                "/certificates/**"
                        ).authenticated()

                        // ========================
                        // 4️ Mặc định: chặn tất cả còn lại
                        // ========================
                        .anyRequest().denyAll()
                )

                // Stateless (chuẩn RESTful API)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Thêm JWT filter trước UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // AuthenticationManager dùng trong AuthService (login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
