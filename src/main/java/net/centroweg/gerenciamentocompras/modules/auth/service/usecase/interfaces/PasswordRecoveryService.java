package net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.mail.MessagingException;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.NewPassword;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.Recovery;

public interface PasswordRecoveryService {

    public void validateAndGenerateRecoveryToken(@RequestBody Recovery recovery) throws MessagingException;
    public void prepareAndSendEmail(Recovery recovery, String token) throws MessagingException;
    public void changePasswordWhenValidToken(NewPassword newPassword, String token);

}
