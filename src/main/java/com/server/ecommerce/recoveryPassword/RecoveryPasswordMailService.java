package com.server.ecommerce.recoveryPassword;

import com.server.ecommerce.recoveryPassword.dto.RecoveryPasswordMailDTO;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class RecoveryPasswordMailService {

    private final JavaMailSender mailSender;

    private final ResourceLoader resourceLoader;

    @Autowired
    public RecoveryPasswordMailService(JavaMailSender javaMailSender, ResourceLoader resourceLoader){
        this.mailSender = javaMailSender;
        this.resourceLoader = resourceLoader;
    }

    public void sendEmail(RecoveryPasswordMailDTO recoveryPasswordMailDTO) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

        Resource resource = resourceLoader.getResource("classpath:templates/RecoveryPassword.html");
        String html = resource.getContentAsString(StandardCharsets.UTF_8);

        html = html.replace("${LINK}", this.getLinkByRecoveryPassword(recoveryPasswordMailDTO.recoveryPassword()));

        helper.setTo(recoveryPasswordMailDTO.to());
        helper.setSubject(recoveryPasswordMailDTO.subject());
        helper.setSubject(recoveryPasswordMailDTO.subject());
        helper.setText(html, true);

        mailSender.send(message);
    }

    private String getLinkByRecoveryPassword(RecoveryPassword recoveryPassword){
        return String.format("http://localhost:3000/reset-password?token=%s", recoveryPassword.getToken());
    }
}
