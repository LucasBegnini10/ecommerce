package com.server.ecommerce.cart.dto;

import java.util.UUID;

public record CartUpdateQuantityDTO(String userId, String productId, long quantity) {
}
