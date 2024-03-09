package com.server.ecommerce.auth.dto;

import com.server.ecommerce.user.User;

public record AuthResponseDTO (User user, String token) {
}
