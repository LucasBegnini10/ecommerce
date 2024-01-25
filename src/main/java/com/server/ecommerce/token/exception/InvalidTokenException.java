package com.server.ecommerce.token.exception;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(){
        super("Invalid token");
    }

    public InvalidTokenException(String message){
        super(message);
    }

}
