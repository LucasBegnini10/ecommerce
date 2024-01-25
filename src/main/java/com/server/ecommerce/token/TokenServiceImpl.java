package com.server.ecommerce.token;

import com.server.ecommerce.token.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.util.*;

@Service("tokenServiceImpl")
public class TokenServiceImpl implements TokenService{

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long tokenExpiration;

    @Override
    public void validateToken(String token) throws InvalidTokenException {
        try {
            extractAllClaims(token);

        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }

    private Jws<Claims> extractAllClaims(String token){
        return  Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token);
    }

    @Override
    public String generateToken(Authentication authentication) {
        Map<String, String> claims = new HashMap<>();

        claims.put("email", authentication.getName());
        claims.put("authorities", populateAuthorities(authentication));

        return Jwts.builder()
        .header()
        .and()
        .subject("Ecommerce")
        .id(UUID.randomUUID().toString())
        .claims(claims)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
        .signWith(getSignInKey())
        .compact();
    }

    private String populateAuthorities(Authentication authentication) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            authoritiesSet.add(authority.getAuthority());
        }

        return String.join(",", authoritiesSet);
    }

    public String extractUserName(String token){
        Claims claims = extractAllClaims(token).getPayload();
        return String.valueOf(claims.get("username"));
    }

    public String extractAuthorities(String token){
        Claims claims = extractAllClaims(token).getPayload();
        return (String) claims.get("authorities");
    }

    @Override
    public SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
