package net.centroweg.gerenciamentocompras.modules.delivery.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeliveryStatusNotFoundException extends BusinessException {

    public DeliveryStatusNotFoundException() {
        super("Status da entrega nao encontrado!", HttpStatus.NOT_FOUND);
    }
}
