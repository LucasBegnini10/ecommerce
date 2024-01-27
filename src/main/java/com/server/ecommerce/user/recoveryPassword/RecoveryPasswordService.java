package com.server.ecommerce.user.recoveryPassword;

import com.server.ecommerce.user.User;
import com.server.ecommerce.user.UserService;
import com.server.ecommerce.user.recoveryPassword.dto.RecoveryPasswordDTO;
import com.server.ecommerce.user.recoveryPassword.dto.RecoveryPasswordMailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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

    public void recovery(RecoveryPasswordDTO recoveryPasswordDTO) {
        User user = userService.findUserByEmail(recoveryPasswordDTO.email());

        disableOtherTokens(user);

        RecoveryPassword recoveryPassword = this.buildRecoveryPassword(user);
        recoveryPasswordRepository.save(recoveryPassword);

        try {

            RecoveryPasswordMailDTO recoveryPasswordMailDTO =
                    new RecoveryPasswordMailDTO("lucasbegnini.dev@gmail.com", "Recuperação de Senha", recoveryPassword, user);

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

    private String generateToken(UUID userId){
        return userId + "#" + UUID.randomUUID();
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
