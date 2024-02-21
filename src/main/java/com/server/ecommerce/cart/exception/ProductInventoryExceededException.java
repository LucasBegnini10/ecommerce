package com.server.ecommerce.cart.exception;

import com.server.ecommerce.exception.BadRequestException;

public class ProductInventoryExceededException extends BadRequestException {

    public ProductInventoryExceededException(){
        super("Product Inventory Exceeded");
    }
}
