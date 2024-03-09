package com.server.ecommerce.user;

import lombok.Getter;

@Getter
public enum RoleType {
    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    RoleType(String role) {
        this.role = role;
    }

    public String getRoleName(){
        return this.name();
    }
}