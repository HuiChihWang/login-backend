package com.example.loginbackend.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility {
    private static final String JWT_SECRET_KEY = "6B5970337336763979244226452948404D6351655468576D5A7134743777217A";
    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    private static final int JWT_TOKEN_LIFE_HOURS = 24;

    public String getJwtTokenFromAuthHeader(String header) {
        if (header == null || !header.startsWith(JWT_TOKEN_PREFIX)) {
            return null;
        }

        return header.substring(JWT_TOKEN_PREFIX.length());
    }

    public String getUsernameFromToken(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserDetails user) {
        if (token == null || user == null) {
            return false;
        }

        String userNameInToken = getUsernameFromToken(token);
        Date expirationDateInToken = getExpirationDateFromToken(token);

        if (userNameInToken == null || !userNameInToken.equals(user.getUsername())) {
            return false;
        }

        Date now = Date.from(Instant.now());
        return expirationDateInToken != null && !expirationDateInToken.before(now);
    }

    public String issueToken(UserDetails user) {
        return issueToken(user, new HashMap<>());
    }

    public String issueToken(UserDetails user, Map<String, ?> extraClaims) {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(Duration.ofHours(JWT_TOKEN_LIFE_HOURS));

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(java.util.Date.from(expirationTime))
                .setClaims(extraClaims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaims(String token, Function<Claims, T> getter) {
        final Claims claims = extractClaims(token);
        return getter.apply(claims);
    }

    private Claims extractClaims(String token) {
        Key siginInKey = getSignInKey();
        JwtParser parser = Jwts.parserBuilder().setSigningKey(siginInKey).build();
        return parser.parseClaimsJwt(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }



}
