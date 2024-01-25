package com.server.ecommerce.auth;

import com.server.ecommerce.auth.dto.AuthDTO;
import com.server.ecommerce.token.TokenService;
import com.server.ecommerce.user.User;
import com.server.ecommerce.user.UserService;
import com.server.ecommerce.user.dto.UserRegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @Autowired
    public AuthService(
            PasswordEncoder passwordEncoder,
            UserService userService,
            AuthenticationManager authenticationManager,
            @Qualifier("tokenServiceImpl") TokenService tokenService
    ){
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }


    public User register(UserRegisterDTO userRegisterDTO){
        User user = userService.buildUserRegister(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userService.createUser(user);
    }

    public String auth(AuthDTO authDTO){
        Authentication authenticated = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password())
        );

        return tokenService.generateToken(authenticated);

    }
}
