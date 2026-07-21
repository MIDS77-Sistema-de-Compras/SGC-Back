package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Exceção lançada quando se tenta inativar uma {@link Request} que já está inativa.
 */
public class RequestAlreadyInactiveException extends BusinessException {
    public RequestAlreadyInactiveException() {
        super("Solicitação já inativa!", HttpStatus.CONFLICT);
    }
}
