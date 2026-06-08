package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestNotFoundException extends BusinessException {
    public RequestNotFoundException() {
        super("Solicitação não encontrada!", HttpStatus.NOT_FOUND);
    }
}
