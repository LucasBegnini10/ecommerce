package com.server.ecommerce.user.exceptions;

import com.server.ecommerce.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(){
        super("User not found");
    }
}
