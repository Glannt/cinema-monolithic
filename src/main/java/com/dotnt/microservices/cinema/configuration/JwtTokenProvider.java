package com.dotnt.microservices.cinema.configuration;

import com.dotnt.microservices.cinema.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    String jwtSecret;

    @Value("${jwt.expiration}")
    Long jwtExpiration;

    private SecretKey Key;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey(){
        // decode secret key to signing key
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return Key;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .decryptWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateTokenWithExceptions(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            // Handle expired token
            throw new BadCredentialsException("Token has expired");
        } catch (MalformedJwtException e) {
            // Handle invalid token format
            throw new BadCredentialsException("Invalid token format");
        } catch (SignatureException e) {
            // Handle signature validation failure
            throw new BadCredentialsException("Invalid token signature");
        } catch (Exception e) {
            // Handle other exceptions
            throw new BadCredentialsException("Token validation failed: " + e.getMessage());
        }
    }
    private Claims exstractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSigningKey())    // create signing key to decoded token
                .build()
                .parseSignedClaims(token)
                .getPayload();   // get all Claims within token
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = exstractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }
}
