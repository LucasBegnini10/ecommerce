package com.server.ecommerce.recoveryPassword.exception;

public class RecoveryTokenExpired extends RecoveryPasswordException{

    public RecoveryTokenExpired(){
        super("Recovery Token expired");
    }
}
