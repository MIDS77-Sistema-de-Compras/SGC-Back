package net.centroweg.gerenciamentocompras.shared.email.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    
    private final JavaMailSender mailSender;

    public void sendEmail(DefaultEmail email, String content) throws MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email.getSendTo());
        helper.setSubject(email.getSubject());

        helper.setText(content, true);

        ClassPathResource logoSenai = new ClassPathResource("assets/logos/senaiLogo.png");
        ClassPathResource logoSgs = new ClassPathResource("assets/logos/sgsLogo.png");

        helper.addInline("logoSenai", logoSenai);
        helper.addInline("logoSgs", logoSgs);

        mailSender.send(message);
    }
    
}
