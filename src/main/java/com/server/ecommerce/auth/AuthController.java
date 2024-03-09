package com.server.ecommerce.auth;

import com.server.ecommerce.auth.dto.AuthDTO;
import com.server.ecommerce.auth.dto.AuthResponseDTO;
import com.server.ecommerce.user.dto.UserRegisterDTO;
import com.server.ecommerce.infra.RestResponseHandler;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> auth(@RequestBody AuthDTO authDTO, HttpServletResponse response){
        AuthResponseDTO authResponseDTO = authService.auth(authDTO);
        String token = authResponseDTO.token();
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return RestResponseHandler.generateResponse(
                "Authenticated",
                HttpStatus.OK,
                authResponseDTO
            );
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getUserByEmail(@CookieValue("token") String token) {
        return RestResponseHandler.generateResponse(
                "User found!",
                HttpStatus.OK,
                authService.loadUserByToken(token)
        );
    }
}
