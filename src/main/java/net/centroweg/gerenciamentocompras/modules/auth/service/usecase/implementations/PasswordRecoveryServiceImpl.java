package net.centroweg.gerenciamentocompras.modules.auth.service.usecase.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces.PasswordRecoveryService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.Recovery;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApiImpl;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailButton;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailFooter;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailLayout;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailParagraph;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailTitle;
import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {
    
    private final JwtService jwtService;
    private final UserPublicApiImpl userPublicApiImpl;
    private final EmailSenderService mailSender;

    @Override
    public void validateAndGenerateRecoveryToken(Recovery recovery) throws MessagingException{
        if(!userPublicApiImpl.existsByEmail(recovery.email())){
            prepareAndSendEmail(recovery, null); // send anyways to prevent invaders from detecting valid emails
            return;
        }

        UserPrincipal user = userPublicApiImpl.findByEmailOrCpf(recovery.email(), null).map(action -> {
            return new UserPrincipal(action);
        }).orElse(null);

        String token = jwtService.generateToken(user);
        prepareAndSendEmail(recovery, token);
    }

    @Override
    public void prepareAndSendEmail(Recovery recovery, String token) throws MessagingException{
        EmailLayout layout = new EmailLayout("Recuperação de Senha",
            List.<EmailBuilder>of(
                new EmailTitle("Recuperação de Senha"),
                new EmailParagraph("Foi solicitada uma mudança de senha para a sua conta (" + recovery.email() + ").", "#666666", 14),
                new EmailParagraph("Clique no botão abaixo para recuperar sua conta.", "#666666", 14),
                new EmailButton("#", "Recuperar Conta"),
                new EmailFooter()
            )
        );

        String text = layout.buildHtml();

        mailSender.sendEmail(new DefaultEmail("Recuperação de Senha", recovery.email()), text);
    }

}
