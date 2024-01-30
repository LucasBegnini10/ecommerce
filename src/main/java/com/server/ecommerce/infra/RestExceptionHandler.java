package com.server.ecommerce.infra;

import com.server.ecommerce.exception.BadRequestException;
import com.server.ecommerce.exception.NotFoundException;
import com.server.ecommerce.recoveryPassword.exception.RecoveryPasswordException;
import com.server.ecommerce.recoveryPassword.exception.RecoveryTokenNotFound;
import com.server.ecommerce.token.exception.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SQLException.class)
    private ResponseEntity<Object> sqlException(SQLException exception){
        return RestResponseHandler.generateResponse("Error", HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    private ResponseEntity<Object> invalidTokenException(InvalidTokenException ex){
        return RestResponseHandler.generateResponse("Invalid token!", HttpStatus.UNAUTHORIZED, null);
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<Object> notFoundException(NotFoundException ex){
        return RestResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(BadRequestException.class)
    private ResponseEntity<Object> badRequestException(BadRequestException ex){
        return RestResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(RecoveryPasswordException.class)
    private ResponseEntity<Object> recoveryPasswordException(RecoveryPasswordException ex){

        HttpStatus status = HttpStatus.BAD_REQUEST;

        if(ex instanceof RecoveryTokenNotFound){
            status = HttpStatus.NOT_FOUND;
        }

        return RestResponseHandler.generateResponse(ex.getMessage(), status, null);
    }
}