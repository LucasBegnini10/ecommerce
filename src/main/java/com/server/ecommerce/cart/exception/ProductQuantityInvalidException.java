package com.server.ecommerce.cart.exception;

import com.server.ecommerce.exception.BadRequestException;

public class ProductQuantityInvalidException extends BadRequestException {
    public ProductQuantityInvalidException(){
        super("Product quantity invalid");
    }
}
