package edu.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // Генерация ключа
    private SecretKey getSigningKey() {
        // Минимум 256 бит для HS256
        String SECRET = "your_super_secret_key_which_should_be_long_enough";
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // Генерация токена
    public String generateTokenSession(String email) {
        // 20 минут
        long EXPIRATION_TIME_SESSION = 20 * 60 * 1000;
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_SESSION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTokenVerify(String email) {
        // 24 часа
        long EXPIRATION_TIME_VERIFY = 24 * 60 * 60 * 1000;
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_VERIFY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Проверка токена
    public boolean isInvalidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // Можно логировать разные ошибки: ExpiredJwtException, MalformedJwtException и т.п.
            return false;
        }
    }

    // Извлечение email из токена
    public String extractEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
