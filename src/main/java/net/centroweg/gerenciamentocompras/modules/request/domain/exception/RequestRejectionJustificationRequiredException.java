package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Exceção lançada quando uma {@link Request} é recusada sem justificativa informada.
 */
public class RequestRejectionJustificationRequiredException extends BusinessException {

    public RequestRejectionJustificationRequiredException() {
        super("A justificativa é obrigatória ao recusar uma solicitação!", HttpStatus.BAD_REQUEST);
    }
}