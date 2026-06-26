package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CrNotEditableException extends BusinessException {


    public CrNotEditableException() {
        super("O CR da solicitação não pode ser alterado pois saiu do status 'EM ANÁLISE'.", HttpStatus.UNPROCESSABLE_CONTENT);
    }


}

