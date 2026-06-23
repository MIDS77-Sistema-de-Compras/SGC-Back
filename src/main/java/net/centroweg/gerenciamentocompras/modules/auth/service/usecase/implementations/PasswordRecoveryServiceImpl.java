package net.centroweg.gerenciamentocompras.modules.auth.service.usecase.implementations;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.domain.exception.InvalidTokenException;
import net.centroweg.gerenciamentocompras.modules.auth.service.CustomUserDetailsService;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.modules.auth.service.api.AuthPublicApi;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces.PasswordRecoveryService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.NewPassword;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.Recovery;
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
    private final AuthPublicApi authPublicApi;
    private final EmailSenderService mailSender;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void validateAndGenerateRecoveryToken(Recovery recovery) throws MessagingException{
        if(!authPublicApi.existsByEmail(recovery.email())){
            prepareAndSendEmail(recovery, null); // send anyways to prevent invaders from detecting valid emails
            return;
        }

        UserPrincipal user = authPublicApi.findByEmailOrCpf(recovery.email(), null).map(action -> {
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
                new EmailButton("http://localhost:3000/nova-senha?token=" + token, "Recuperar Conta"),
                new EmailFooter()
            )
        );

        String text = layout.buildHtml();

        mailSender.sendEmail(new DefaultEmail("Recuperação de Senha", recovery.email()), text);
    }

    @Override
    @Transactional
    public void changePasswordWhenValidToken(NewPassword newPassword, String token){
        String validToken = jwtService.validateToken(token);

        if(validToken == null){
            throw new InvalidTokenException("Token de identificação inválido ou inexistente, certifique-se de acessar a página pelo link enviado pelo E-mail.");
        }

        UserPrincipal details = (UserPrincipal) userDetailsService.loadUserByUsername(validToken);

        authPublicApi.findByEmailOrCpf(details.getUsername(), null).ifPresent(action -> {
            action.setPassword(passwordEncoder.encode(newPassword.password()));
        });
    }
}
