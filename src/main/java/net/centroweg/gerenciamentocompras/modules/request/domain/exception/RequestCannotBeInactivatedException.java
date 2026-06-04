package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestCannotBeInactivatedException extends BusinessException {
    public RequestCannotBeInactivatedException() {
        super("Solicitação não pode ser inativada após aprovação.", HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
