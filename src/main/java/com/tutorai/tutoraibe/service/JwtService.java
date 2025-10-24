package com.tutorai.tutoraibe.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${app.jwt.secret}") private String secret;
    @Value("${app.jwt.expiration}") private long accessMs;
    @Value("${app.jwt.refresh-expiration}") private long refreshMs;

    private byte[] key() { return secret.getBytes(StandardCharsets.UTF_8); }

    public String generateAccessToken(String subject, Map<String,Object> claims){
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessMs);
        return Jwts.builder()
                .setSubject(subject).addClaims(claims)
                .setIssuedAt(now).setExpiration(exp)
                .signWith(Keys.hmacShaKeyFor(key()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(String subject){ return generateAccessToken(subject, Map.of()); }

    public String generateRefreshToken(String subject){
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshMs);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now).setExpiration(exp)
                .signWith(Keys.hmacShaKeyFor(key()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token){
        return parse(token).getBody().getSubject();
    }

    public boolean isValid(String token, String subject){
        try {
            var claims = parse(token).getBody();
            return subject.equals(claims.getSubject()) && claims.getExpiration().after(new Date());
        } catch (JwtException e){ return false; }
    }

    private Jws<Claims> parse(String token){
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(key())).build().parseClaimsJws(token);
    }
}
