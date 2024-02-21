package com.server.ecommerce.cart.dto;

import java.util.UUID;

public record CartUpdateQuantityDTO(UUID userId, UUID productId, long quantity) {
}
