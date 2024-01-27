package com.server.ecommerce.recoveryPassword.exception;

public class RecoveryTokenUsed extends RecoveryPasswordException{

    public RecoveryTokenUsed(){
        super("Recovery Token used");
    }
}
