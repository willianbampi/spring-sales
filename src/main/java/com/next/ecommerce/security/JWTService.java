package com.next.ecommerce.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.next.ecommerce.domain.entity.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTService {

    @Value("${security.jwt.expiration-time}")
    private String expirationTime;

    @Value("${security.jwt.signature-key}")
    private String signatureKey;

    public String tokenGenerate(User user) {
        Long addExpirationTime = Long.valueOf(expirationTime);
        LocalDateTime expireIn = LocalDateTime.now().plusMinutes(addExpirationTime);
        Instant expireInstant = expireIn.atZone(ZoneId.systemDefault()).toInstant();
        Date expireDate = Date.from(expireInstant);
        return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setExpiration(expireDate)
                    .signWith(SignatureAlgorithm.HS512, signatureKey)
                    .compact();
    }

    public Claims getClaimsFromToken(String token) throws ExpiredJwtException {
        return Jwts.parser()
                    .setSigningKey(signatureKey)
                    .parseClaimsJws(token)
                    .getBody();
    }

    public boolean isValidToken(String token) {
        try {
            Claims returnedClaims = getClaimsFromToken(token);
            Date expireDate = returnedClaims.getExpiration();
            LocalDateTime expireIn = expireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return LocalDateTime.now().isBefore(expireIn);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) throws ExpiredJwtException {
        return (String) getClaimsFromToken(token).getSubject();
    }
    
}
