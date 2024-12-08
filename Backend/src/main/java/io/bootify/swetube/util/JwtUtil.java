package io.bootify.swetube.util;

import io.bootify.swetube.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
@Component
public class JwtUtil {

    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long expirationTime = 86400000; // 1 dag i millisekunder

    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 1 dag
                .signWith(secretKey) // Använd rätt hemlig nyckel
                .compact();
    }
}
