package com.server.ecommerce.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

    public PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordUtils(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public String hashPwd(String pwd){
        return this.passwordEncoder.encode(pwd);
    }

    public boolean comparePwd(String pwd, String hashedPwd){
        return this.passwordEncoder.matches(pwd, hashedPwd);
    }

}
