package com.server.ecommerce.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDTO {

    private String name;
    private String email;
    private String password;
    private String phone;
}
