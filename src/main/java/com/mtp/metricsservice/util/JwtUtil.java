package com.mtp.metricsservice.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Base64;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") Long expiration) {
        byte[] decodedSecret = Base64.getDecoder().decode(secret);
        this.secretKey = new SecretKeySpec(decodedSecret, SignatureAlgorithm.HS256.getJcaName());
        this.expiration = expiration;
    }

    public String generateToken(String clientId) {
        return Jwts.builder()
                .setSubject(clientId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, String clientId) {
        final String clientFromToken = getClientIdFromToken(token);
        if (clientFromToken == null) {
            return false;
        }
        return (clientFromToken.equals(clientId) && !isTokenExpired(token));
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getClientIdFromToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            String subject = claims.getSubject();
            return (subject != null && !subject.isEmpty()) ? subject : null;
        } catch (Exception e) {
            return null;
        }
    }


    private boolean isTokenExpired(String token) {
        final Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}
