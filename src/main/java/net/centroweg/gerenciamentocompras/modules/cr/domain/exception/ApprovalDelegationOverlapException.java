package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ApprovalDelegationOverlapException extends BusinessException {

    public ApprovalDelegationOverlapException() {
        super("Já existe uma delegação pendente ou ativa sobreposta para o supervisor.", HttpStatus.CONFLICT);
    }
}
