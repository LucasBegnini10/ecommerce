package com.server.ecommerce.token;

import com.server.ecommerce.token.exception.InvalidTokenException;
import org.springframework.security.core.Authentication;

import java.security.Key;

public interface TokenService {

    void validateToken(String token) throws InvalidTokenException;

    String generateToken(Authentication authentication);

    Key getSignInKey();

}
