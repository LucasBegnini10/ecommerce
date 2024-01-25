package com.server.ecommerce.role;

import lombok.Getter;

@Getter
public enum RoleType {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private final String role;

    RoleType(String role) {
        this.role = role;
    }

    public String getRoleName(){
        return this.name();
    }
}