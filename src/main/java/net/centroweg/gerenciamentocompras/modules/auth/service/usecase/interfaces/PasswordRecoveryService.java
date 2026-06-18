package net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces;

import jakarta.mail.MessagingException;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.Recovery;

public interface PasswordRecoveryService {

    public String validateAndGenerateRecoveryToken(Recovery recovery) throws MessagingException;
    public void prepareAndSendEmail(Recovery recovery, String token) throws MessagingException;

}
