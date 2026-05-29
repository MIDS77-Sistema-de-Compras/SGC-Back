package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CrBranchAlreadyExistsException extends BusinessException {
    public CrBranchAlreadyExistsException() {
        super("CR já vinculado a esta filial", HttpStatus.CONFLICT);
    }
}
