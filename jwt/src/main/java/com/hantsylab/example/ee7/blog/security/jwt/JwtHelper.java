package com.hantsylab.example.ee7.blog.security.jwt;

import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author hantsy
 */
@ApplicationScoped
public class JwtHelper {

    private final String secret = "test123";

    private final long expirationInSeconds = 3600 * 24;//one day

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + this.expirationInSeconds * 1000);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getUsername());
        claims.put("role", user.getRole().name());

        return generateTokenFromClaims(claims);
    }

    public UserPrincipal parseToken(String token) throws Exception {
        Claims claims = extractClaimsFromToken(token);
        if (claimsIsExpired(claims)) {
            throw new RuntimeException("token is expired");
        }
        return new JwtUser((String) claims.get("sub"), Arrays.asList((String) claims.get("role")));
    }

    private boolean claimsIsExpired(Claims claims) throws Exception {
        boolean isExpired = claims.getExpiration().before(new Date());
        return isExpired;
    }

    public String refreshToken(String token) throws Exception {
        Claims claims = extractClaimsFromToken(token);

        if (claimsIsExpired(claims)) {
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

    private Claims extractClaimsFromToken(String token) throws Exception {
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
