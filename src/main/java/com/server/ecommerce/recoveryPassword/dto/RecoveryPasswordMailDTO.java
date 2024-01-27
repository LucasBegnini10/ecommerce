package com.server.ecommerce.recoveryPassword.dto;

import com.server.ecommerce.recoveryPassword.RecoveryPassword;
import com.server.ecommerce.user.User;

public record RecoveryPasswordMailDTO (String to, String subject, RecoveryPassword recoveryPassword, User user){
}
