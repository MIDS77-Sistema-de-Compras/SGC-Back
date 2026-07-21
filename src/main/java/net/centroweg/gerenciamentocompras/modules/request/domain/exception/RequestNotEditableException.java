package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Exceção lançada quando se tenta editar uma {@link Request} que já foi aprovada.
 */
public class RequestNotEditableException extends BusinessException {
    public RequestNotEditableException() {
        super("Solicitação não pode ser editada após aprovação!", HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
