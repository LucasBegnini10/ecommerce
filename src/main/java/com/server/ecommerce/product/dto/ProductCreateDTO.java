package com.server.ecommerce.product.dto;

public record ProductCreateDTO(String name, String description, long price, int quantity, boolean isEnabled){
}
