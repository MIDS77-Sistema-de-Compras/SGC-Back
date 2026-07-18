package net.centroweg.gerenciamentocompras.modules.notification.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * Exceção lançada quando uma {@link Notification} não é encontrada no banco de dados.
 */
public class NotificationNotFoundException extends BusinessException {
    public NotificationNotFoundException() {
        super("Notificação não encontrada!", HttpStatus.NOT_FOUND);
    }
}
