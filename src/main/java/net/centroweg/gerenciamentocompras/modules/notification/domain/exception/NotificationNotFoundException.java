package net.centroweg.gerenciamentocompras.modules.notification.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends BusinessException {
    public NotificationNotFoundException() {
        super("Notificação não encontrada!", HttpStatus.NOT_FOUND);
    }
}
