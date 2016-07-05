package com.hantsylab.example.ee7.blog.security;

import com.hantsylab.example.ee7.blog.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hantsy
 */
public class JwtHelper {

    private final String secret = "test123";

    private final long expirationInSeconds = 3600;

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + this.expirationInSeconds * 1000);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getUsername());

        return generateTokenFromClaims(claims);
    }

    public String parseToken(String token) {
        Claims claims = extractClaimsFromToken(token);

        String username = (String) claims.get("sub");

        return username;
    }

    public String refreshToke(String token) {
        Claims claims = extractClaimsFromToken(token);
        boolean isExpired = claims.getExpiration().before(new Date());

        if (isExpired) {
            return generateTokenFromClaims(claims);
        }
        return token;
    }

    private String generateTokenFromClaims(Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(generateExpirationDate())
            .signWith(SignatureAlgorithm.HS512, this.secret)
            .compact();
    }

    private Claims extractClaimsFromToken(String token) throws RuntimeException {
        Claims claims;
        try {
            claims = Jwts.parser()
                .setSigningKey(this.secret)
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new RuntimeException("token:" + token + " is invalid");
        }
        return claims;
    }

}
