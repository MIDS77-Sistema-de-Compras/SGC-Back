package net.centroweg.gerenciamentocompras.modules.delivery.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeliveryNotFoundException extends BusinessException {
    public DeliveryNotFoundException() {
        super("Entrega nao encontrada!", HttpStatus.NOT_FOUND);
    }
}
