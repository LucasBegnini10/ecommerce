package com.server.ecommerce.cart.exception;

import com.server.ecommerce.exception.NotFoundException;

public class CartNotFoundException extends NotFoundException {
    public CartNotFoundException(){
        super("Cart not found");
    }
}
