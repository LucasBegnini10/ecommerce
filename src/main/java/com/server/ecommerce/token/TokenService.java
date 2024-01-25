package com.server.ecommerce.token;

import com.server.ecommerce.token.exception.InvalidTokenException;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;

public interface TokenService {

    void validateToken(String token) throws InvalidTokenException;

    String generateToken(Authentication authentication);

    SecretKey getSignInKey();

    String extractUserName(String token);

    String extractAuthorities(String token);

}
