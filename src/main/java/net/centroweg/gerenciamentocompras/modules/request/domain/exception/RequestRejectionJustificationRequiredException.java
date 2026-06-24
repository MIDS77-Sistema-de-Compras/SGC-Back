package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestRejectionJustificationRequiredException extends BusinessException {

    public RequestRejectionJustificationRequiredException() {
        super("A justificativa é obrigatória ao recusar uma solicitação.", HttpStatus.BAD_REQUEST);
    }
}