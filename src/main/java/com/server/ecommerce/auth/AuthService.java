package com.server.ecommerce.auth;

import com.server.ecommerce.auth.dto.AuthDTO;
import com.server.ecommerce.token.TokenService;
import com.server.ecommerce.user.User;
import com.server.ecommerce.user.UserService;
import com.server.ecommerce.user.dto.UserRegisterDTO;
import com.server.ecommerce.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordUtils passwordUtils;

    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @Autowired
    public AuthService(
            PasswordUtils passwordUtils,
            UserService userService,
            @Qualifier("tokenServiceImpl") TokenService tokenService
    ){
        this.passwordUtils = passwordUtils;
        this.userService = userService;
        this.tokenService = tokenService;
    }


    public User register(UserRegisterDTO userRegisterDTO){
        User user = userService.buildUserRegister(userRegisterDTO);
        user.setPassword(passwordUtils.hashPwd(user.getPassword()));

        return userService.createUser(user);
    }

    public String auth(AuthDTO authDTO){
        Authentication authenticated = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password())
        );

        return tokenService.generateToken(authenticated);
    }
}
