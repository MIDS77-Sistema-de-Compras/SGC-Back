package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestAlreadyApprovedException extends BusinessException {
    public RequestAlreadyApprovedException() {
        super("Solicitação já aprovada!", HttpStatus.CONFLICT);
    }
}
