package net.centroweg.gerenciamentocompras.modules.delivery.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeliveryAccessDeniedException extends BusinessException {

    public DeliveryAccessDeniedException() {
        super("Acesso negado por questoes de seguranca", HttpStatus.FORBIDDEN);
    }
}
