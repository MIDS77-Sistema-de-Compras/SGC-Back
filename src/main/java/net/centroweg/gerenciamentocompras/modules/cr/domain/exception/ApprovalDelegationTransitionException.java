package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ApprovalDelegationTransitionException extends BusinessException {

    public ApprovalDelegationTransitionException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
