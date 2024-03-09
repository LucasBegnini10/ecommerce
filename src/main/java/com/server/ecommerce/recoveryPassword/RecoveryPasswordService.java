package com.server.ecommerce.recoveryPassword;

import com.server.ecommerce.recoveryPassword.dto.RecoveryPasswordMailDTO;
import com.server.ecommerce.recoveryPassword.dto.ResetPasswordDTO;
import com.server.ecommerce.recoveryPassword.exception.RecoveryTokenDisabled;
import com.server.ecommerce.recoveryPassword.exception.RecoveryTokenExpired;
import com.server.ecommerce.recoveryPassword.exception.RecoveryTokenUsed;
import com.server.ecommerce.recoveryPassword.exception.RecoveryTokenNotFound;
import com.server.ecommerce.user.User;
import com.server.ecommerce.user.UserService;
import com.server.ecommerce.recoveryPassword.dto.RecoveryPasswordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecoveryPasswordService {

    private final UserService userService;

    private final RecoveryPasswordRepository recoveryPasswordRepository;

    private final RecoveryPasswordMailService recoveryPasswordMailService;

    @Autowired
    public RecoveryPasswordService(
            UserService userService,
            RecoveryPasswordRepository recoveryPasswordRepository,
            RecoveryPasswordMailService recoveryPasswordMailService
    ){
        this.userService = userService;
        this.recoveryPasswordRepository = recoveryPasswordRepository;
        this.recoveryPasswordMailService = recoveryPasswordMailService;
    }

    public void resetPassword(ResetPasswordDTO  resetPasswordDTO){
        RecoveryPassword recoveryPassword = findRecoveryActiveByToken(resetPasswordDTO.token());

        recoveryPassword.setUsed(true);
        recoveryPassword.setUsedIn(LocalDateTime.now());
        recoveryPassword.setEnabled(false);

        recoveryPasswordRepository.save(recoveryPassword);

        userService.updatePassword(recoveryPassword.getUser(), resetPasswordDTO.password());
    }

    public RecoveryPassword findRecoveryActiveByToken(String token){
        Optional<RecoveryPassword> recoveryByToken = recoveryPasswordRepository.findByToken(token);

        if(recoveryByToken.isEmpty()) {
            throw new RecoveryTokenNotFound();
        }

        RecoveryPassword recoveryPassword = recoveryByToken.get();

        validateToken(recoveryPassword);

        return recoveryPassword;
    }

    private void validateToken(RecoveryPassword recoveryPassword){
        if(recoveryPassword.isUsed()) {
            throw new RecoveryTokenUsed();
        }

        if(!recoveryPassword.isEnabled()){
            throw new RecoveryTokenDisabled();
        }

        if(tokenIsExpired(recoveryPassword)){
            throw new RecoveryTokenExpired();
        }
    }

    private boolean tokenIsExpired(RecoveryPassword recoveryPassword){
        return  recoveryPassword.getExpiresIn().isBefore(LocalDateTime.now());
    }

    public void recovery(RecoveryPasswordDTO recoveryPasswordDTO) {
        User user = userService.findUserByEmail(recoveryPasswordDTO.email());

        disableOtherTokens(user);

        RecoveryPassword recoveryPassword = this.buildRecoveryPassword(user);
        recoveryPasswordRepository.save(recoveryPassword);

        try {
            RecoveryPasswordMailDTO recoveryPasswordMailDTO =
                    new RecoveryPasswordMailDTO(recoveryPasswordDTO.email(), "Recuperação de Senha", recoveryPassword, user);

            recoveryPasswordMailService.sendEmail(recoveryPasswordMailDTO);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private RecoveryPassword buildRecoveryPassword(User user){
        RecoveryPassword recoveryPassword =  new RecoveryPassword();
        String token = this.generateToken(user.getId());

        recoveryPassword.setUser(user);
        recoveryPassword.setToken(token);
        recoveryPassword.setExpiresIn(getExpiresIn());
        recoveryPassword.setUsed(false);
        recoveryPassword.setEnabled(true);

        return recoveryPassword;
    }

    private String generateToken(String userId){
        return userId + "$" + UUID.randomUUID();
    }

    private LocalDateTime getExpiresIn(){
        LocalDateTime now = LocalDateTime.now();
        return now.plusHours(3);
    }

    private void disableOtherTokens(User user){
        List<RecoveryPassword> enables = recoveryPasswordRepository.findByUserIdAndIsEnabledTrue(user.getId());


        for(RecoveryPassword recoveryPassword : enables){
            recoveryPassword.setEnabled(false);
        }

        recoveryPasswordRepository.saveAll(enables);
    }
}
