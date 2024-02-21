package com.server.ecommerce.user.dto;

public record UserUpdatePasswordDTO(String currentPassword, String newPassword) {
}
