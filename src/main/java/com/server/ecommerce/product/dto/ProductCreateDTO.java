package com.server.ecommerce.product.dto;

public record ProductCreateDTO(String name, String description, int price, int quantity, boolean isEnabled){
}
