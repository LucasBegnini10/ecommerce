package com.server.ecommerce.user.exceptions;

import com.server.ecommerce.exception.BadRequestException;

public class InvalidPasswordException extends BadRequestException {

    public InvalidPasswordException(){
        super("Invalid password");
    }
}
