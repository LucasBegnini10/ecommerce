package com.server.ecommerce.cart.exception;

import com.server.ecommerce.exception.NotFoundException;

public class ProductNotFoundInCartException extends NotFoundException {
    public ProductNotFoundInCartException(){
        super("Product Not Found In Cart Exception");
    }
}
