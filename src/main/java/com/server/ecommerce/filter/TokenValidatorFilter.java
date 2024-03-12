package com.server.ecommerce.filter;

import com.server.ecommerce.token.TokenServiceImpl;
import com.server.ecommerce.token.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
public class TokenValidatorFilter extends OncePerRequestFilter {


    private final TokenServiceImpl tokenService;

    @Autowired
    public TokenValidatorFilter(
            TokenServiceImpl tokenService
    ) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            String token = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    System.out.println("COOKIE ==> " + cookie.getName());
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }

            if(token == null){
                filterChain.doFilter(request, response);
                return;
            }

            tokenService.validateToken(token);

            String username = tokenService.extractUserName(token);
            String authorities = tokenService.extractAuthorities(token);

            Authentication auth = buildAuthentication(
                    username,
                    null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

            setAuthentication(auth);

        } catch (InvalidTokenException ex){
            System.out.println("Invalid token ==> " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }


    private Authentication buildAuthentication(
            Object principal,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities
    ){
        return new UsernamePasswordAuthenticationToken(
                principal,
                credentials,
                authorities
        );
    }

    private void setAuthentication(Authentication auth){
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
