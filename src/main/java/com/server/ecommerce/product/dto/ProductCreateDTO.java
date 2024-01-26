package com.server.ecommerce.product.dto;

public record ProductCreateDTO(String name, String description, float price, int quantity) {
}
