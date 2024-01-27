package com.server.ecommerce.recoveryPassword.exception;

public class RecoveryTokenNotFound extends RecoveryPasswordException{

    public RecoveryTokenNotFound(){
        super("Recovery Token not found");
    }
}
