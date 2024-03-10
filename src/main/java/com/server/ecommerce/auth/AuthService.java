package com.server.ecommerce.auth;

import com.server.ecommerce.auth.dto.AuthDTO;
import com.server.ecommerce.auth.dto.AuthResponseDTO;
import com.server.ecommerce.token.TokenServiceImpl;
import com.server.ecommerce.user.User;
import com.server.ecommerce.user.UserService;
import com.server.ecommerce.user.dto.UserRegisterDTO;
import com.server.ecommerce.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final TokenServiceImpl tokenService;

    @Autowired
    public AuthService(
            PasswordUtils passwordUtils,
            UserService userService,
            TokenServiceImpl tokenService
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

    public AuthResponseDTO auth(AuthDTO authDTO){
        Authentication authenticated = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password())
        );

        String token = tokenService.generateToken(authenticated);

        return new AuthResponseDTO((User) authenticated.getPrincipal(), token);
    }

    public User loadUserByToken(String token){
        String email = tokenService.extractUserName(token);
        return userService.findUserByEmail(email);
    }
}
