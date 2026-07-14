package net.centroweg.gerenciamentocompras.modules.delivery.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeliveryAlreadyInactiveException extends BusinessException {
    public DeliveryAlreadyInactiveException() {
        super("Entrega ja inativa!", HttpStatus.CONFLICT);
    }
}
