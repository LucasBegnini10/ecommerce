package com.server.ecommerce.token;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service("tokenServiceImpl")
public class TokenServiceImpl implements TokenService{

    @Override
    public void validateToken(String token) throws BadCredentialsException {

    }

    @Override
    public String generateToken(Authentication authentication) {
        return null;
    }

    @Override
    public Key getSignInKey() {
        return null;
    }
}
