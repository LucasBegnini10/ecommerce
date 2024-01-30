package com.server.ecommerce.product.exception;

import com.server.ecommerce.exception.BadRequestException;

public class PictureLimitExceededException extends BadRequestException {

    public PictureLimitExceededException(){
        super("Picture limit exceeded!");
    }
}
