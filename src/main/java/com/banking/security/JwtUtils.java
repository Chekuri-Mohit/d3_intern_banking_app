package com.banking.security;
//
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret = Base64.getEncoder().encodeToString("your256bitsecretyour256bitsecretiuybuyvbuybuyb".getBytes());
    private static final String SECRET_KEY = "your256bitsecretyour256bitsecretiuybuyvbuybuyb";
    private static final long EXPIRATION_TIME = 86400000;
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSigningKey() , SignatureAlgorithm.HS256)
            .compact();
    }
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtSecret)
            .build()
            .parseClaimsJws(token).getBody().getSubject();
    }

}
