package com.tutorai.tutoraibe.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}") private String jwtSecret;
    @Value("${jwt.expiration}") private long jwtExpiration;
    @Value("${jwt.refresh-expiration}") private long jwtRefreshExpiration;

    private Key key() {
        // Dùng bytes trực tiếp để không cần Base64
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Map<String, Object> claims, String subject, long ttlMillis) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttlMillis))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(String email, Map<String, Object> claims) {
        return generateToken(claims, email, jwtExpiration);
    }

    public String generateRefreshToken(String email) {
        return generateToken(Map.of(), email, jwtRefreshExpiration);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> resolver) {
        return resolver.apply(parseAllClaims(token));
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody();
    }

    public boolean isValid(String token, String expectedEmail) {
        try {
            String email = extractEmail(token);
            Date exp = extractClaim(token, Claims::getExpiration);
            return email.equals(expectedEmail) && exp.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
