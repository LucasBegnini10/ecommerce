package com.server.ecommerce.recoveryPassword.exception;

public class RecoveryTokenDisabled extends RecoveryPasswordException{

    public RecoveryTokenDisabled(){
        super("Recovery Token disabled");
    }
}
