package net.centroweg.gerenciamentocompras.modules.delivery.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeliveryReceiverNotFoundException extends BusinessException {
    public DeliveryReceiverNotFoundException() {
        super("Recebedor nao associado a entrega!", HttpStatus.NOT_FOUND);
    }
}
