package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ApprovalDelegationNotFoundException extends BusinessException {

    public ApprovalDelegationNotFoundException(Long id) {
        super("Delegação de aprovação não encontrada com o ID: " + id, HttpStatus.NOT_FOUND);
    }
}
