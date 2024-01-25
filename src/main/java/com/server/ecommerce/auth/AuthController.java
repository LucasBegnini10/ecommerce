package com.server.ecommerce.auth;

import com.server.ecommerce.auth.dto.AuthDTO;
import com.server.ecommerce.user.dto.UserRegisterDTO;
import com.server.ecommerce.infra.RestResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody UserRegisterDTO userRegister){
        return RestResponseHandler.generateResponse(
                "User created",
                HttpStatus.CREATED,
                authService.register(userRegister)
        );
    }

    @PostMapping
    public ResponseEntity<Object> auth(@RequestBody AuthDTO authDTO){
        return RestResponseHandler.generateResponse(
                "Authenticated",
                HttpStatus.OK,
                authService.auth(authDTO)
            );
    }
}
