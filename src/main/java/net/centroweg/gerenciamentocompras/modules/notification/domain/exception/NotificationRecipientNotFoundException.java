package net.centroweg.gerenciamentocompras.modules.notification.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotificationRecipientNotFoundException extends BusinessException {

    public NotificationRecipientNotFoundException(Long userId) {
        super("Destinatario da notificacao nao encontrado com id: " + userId, HttpStatus.NOT_FOUND);
    }
}
