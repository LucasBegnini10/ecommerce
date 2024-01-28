package com.server.ecommerce.product.exception;

import com.server.ecommerce.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException(){
        super("Product not found");
    }
}
