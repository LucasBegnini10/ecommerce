package com.server.ecommerce.product.exception;

import com.server.ecommerce.exception.NotFoundException;

public class ProductPictureNotFoundException extends NotFoundException {

    public ProductPictureNotFoundException(){
        super("Picture not found");
    }
}

