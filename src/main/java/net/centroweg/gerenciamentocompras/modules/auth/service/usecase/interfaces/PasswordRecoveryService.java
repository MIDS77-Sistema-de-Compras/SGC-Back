package net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces;

import jakarta.mail.MessagingException;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.NewPassword;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.Recovery;

/**
 * Define o contrato para o serviço de recuperação de senha do usuário.
 */
public interface PasswordRecoveryService {

    /**
     * Valida a solicitação de recuperação de senha e gera um token de recuperação, quando aplicável.
     * @param recovery objeto contendo os dados informados para recuperação.
     * @throws MessagingException caso ocorra um erro durante o envio do e-mail.
     */
    public void validateAndGenerateRecoveryToken(Recovery recovery) throws MessagingException;

    /**
     * Prepara o conteúdo do e-mail de recuperação de senha e realiza seu envio.
     * @param recovery objeto contendo os dados do destinatário.
     * @param token token de recuperação que será enviado ao usuário.
     * @throws MessagingException caso ocorra algum erro durante o envio do e-mail.
     */
    public void prepareAndSendEmail(Recovery recovery, String token) throws MessagingException;

    /**
     * Altera a senha do usuário após validar o token de recuperação.
     * @param newPassword objeto contendo a nova senha.
     * @param token token de recuperação utilizado para autorizar a atualização.
     */
    public void changePasswordWhenValidToken(NewPassword newPassword, String token);

}
