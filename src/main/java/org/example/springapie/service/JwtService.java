package org.example.springapie.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public long getExpirationTime() {
        return jwtExpiration;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // getSubject - витягне логин, тут витягаємо все по токену
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails); // генеруємо токен по юзернейму
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, jwtExpiration, userDetails); // генеруємо токен з клеймс(наприклад роль, айді та емейли)
    }

    private String buildToken(Map<String, Object> claims, long expiration, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims) // додаткові дані по типу роль,айді та інше
                .setSubject(userDetails.getUsername()) // логін
                .setIssuedAt(new Date(System.currentTimeMillis())) // дата створення
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // дата коли токен зникне
                .signWith(getSignInKey(), SignatureAlgorithm.ES256)
                .compact(); // скидаємо все в один рядок
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // витягуємо юзернем та токен і зрівнюємо чт він дійсний
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // декокудємо код з формату
        // і Створює об'єкт Key, який використовується для підпису/перевірки токена.
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
