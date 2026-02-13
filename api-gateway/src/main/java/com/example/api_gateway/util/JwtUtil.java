package com.example.api_gateway.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    // Validar token
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token);

            // Optional: verificar expiración
            Date expiration = claimsJws.getBody().getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                System.out.println("Token expirado");
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Error validando token: " + e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
