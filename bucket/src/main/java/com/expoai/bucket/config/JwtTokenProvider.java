package com.expoai.bucket.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);

    private final SecretKey jwtSecretKey;
    private JwtParser jwtParser;

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @PostConstruct
    public void init() {
        this.jwtParser = Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build();
    }

    public String generateToken(String username, List<String> roles) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        long expMillis = nowMillis + 3600000; // 1 hour expiration
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(exp)
                .claim("roles", roles)
                .signWith(jwtSecretKey, Jwts.SIG.HS512)
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseSignedClaims(authToken);
            return true;
        } catch (JwtException ex) {
            logger.error("Invalid JWT: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT token is empty or null");
        }
        return false;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = jwtParser
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }
}
