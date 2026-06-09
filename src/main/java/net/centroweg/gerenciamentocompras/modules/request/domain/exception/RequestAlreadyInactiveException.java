package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestAlreadyInactiveException extends BusinessException {
    public RequestAlreadyInactiveException() {
        super("Solicitação já inativa!", HttpStatus.CONFLICT);
    }
}
