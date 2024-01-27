package com.server.ecommerce.user.recoveryPassword.dto;

import com.server.ecommerce.user.User;
import com.server.ecommerce.user.recoveryPassword.RecoveryPassword;

public record RecoveryPasswordMailDTO (String to, String subject, RecoveryPassword recoveryPassword, User user){
}
