package com.server.ecommerce.product.dto;

public record ProductDTO(String name, String description, Long price, Integer quantity, Boolean isEnabled){
}
