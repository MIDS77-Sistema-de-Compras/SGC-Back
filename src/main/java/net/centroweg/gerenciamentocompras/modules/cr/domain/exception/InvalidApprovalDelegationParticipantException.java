package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidApprovalDelegationParticipantException extends BusinessException {

    public InvalidApprovalDelegationParticipantException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
