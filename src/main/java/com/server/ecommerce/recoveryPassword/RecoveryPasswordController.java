package com.server.ecommerce.recoveryPassword;

import com.server.ecommerce.infra.RestResponseHandler;
import com.server.ecommerce.recoveryPassword.dto.RecoveryPasswordDTO;
import com.server.ecommerce.recoveryPassword.dto.ResetPasswordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/recovery-password")
public class RecoveryPasswordController {

    private final RecoveryPasswordService recoveryPasswordService;

    @Autowired
    public RecoveryPasswordController(RecoveryPasswordService recoveryPasswordService) {
        this.recoveryPasswordService = recoveryPasswordService;
    }

    @PostMapping
    public ResponseEntity<Object> recovery(@RequestBody RecoveryPasswordDTO recoveryPasswordDTO) {
        recoveryPasswordService.recovery(recoveryPasswordDTO);
        return RestResponseHandler.generateResponse(
                "Mail sent!",
                HttpStatus.OK,
                null
        );
    }

    @PostMapping("/reset")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO){
        recoveryPasswordService.resetPassword(resetPasswordDTO);
        return RestResponseHandler.generateResponse(
                "Password reset!",
                HttpStatus.OK,
                null
        );
    }

}
