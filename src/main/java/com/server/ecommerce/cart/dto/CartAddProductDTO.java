package com.server.ecommerce.cart.dto;

import java.util.UUID;

public record CartAddProductDTO (UUID productId, UUID userId){
}
