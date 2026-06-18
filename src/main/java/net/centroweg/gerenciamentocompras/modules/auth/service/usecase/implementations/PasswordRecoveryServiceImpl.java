package net.centroweg.gerenciamentocompras.modules.auth.service.usecase.implementations;

import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.Recovery;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApiImpl;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryServiceImpl {
    
    private final JwtService jwtService;
    private final UserPublicApiImpl userPublicApiImpl;
    private final EmailSenderService mailSender;

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

    public void prepareAndSendEmail(Recovery recovery, String token) throws MessagingException{
        String text = """
                <p>Foi solicitada uma mudança de senha para a sua conta (%s)</p>

                <p>Clique no botão abaixo para recuperar sua conta.</p>

                <a href="#MUDAR_QUANDO_FRONT_ESTIVER_PRONTO_COM_API?token=%s" class="btn" target="_blank">Alterar Senha</a>

                <small>Se você não solicitou uma troca de senha, por favor, ignore este email.</small>
                """.formatted(recovery.email(), token);

        mailSender.sendEmail(new DefaultEmail("Recuperação de Senha", recovery.email(), text));
    }

}
