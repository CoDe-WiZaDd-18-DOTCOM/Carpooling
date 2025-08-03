package com.example.carpooling.utils;

import com.example.carpooling.services.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    @Autowired
    private RedisService redisService;

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private SecretKey getSigningKey() {
        log.debug("Generating signing key from SECRET_KEY");
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token) {
        try {
            String id = redisService.get(token, String.class);
            if (id != null) {
                return id;
            }

            Claims claims = extractAllClaims(token);
            id = claims.getSubject();
            redisService.set(token, id, 300L);
            return id;
        } catch (Exception e) {
            log.error("❌ Error extracting username from JWT: {}", e.getMessage());
            return null;
        }
    }

    public String extractRole(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String role = claims.get("role", String.class);
            return role;
        } catch (Exception e) {
            log.error("❌ Error extracting role from JWT: {}", e.getMessage());
            return null;
        }
    }

    public Date extractExpiration(String token) {
        try {
            Date exp = extractAllClaims(token).getExpiration();
            return exp;
        } catch (Exception e) {
            log.error("❌ Error extracting expiration from JWT: {}", e.getMessage());
            return null;
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("❌ Error parsing JWT token: {}", e.getMessage());
            throw e;
        }
    }

    private Boolean isTokenExpired(String token) {
        Date exp = extractExpiration(token);
        if(exp==null) return true;
        boolean expired = exp.before(new Date());
        log.info("Token expiry check:{}",expired);
        return expired;
    }

    public String generateToken(String username, String role) {
        String token = createToken(new HashMap<>() {{
            put("role", role);
        }}, username);
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token) {
        boolean valid = !isTokenExpired(token);
        log.info("🔐 Is token valid? {}", valid);
        return valid;
    }
}
