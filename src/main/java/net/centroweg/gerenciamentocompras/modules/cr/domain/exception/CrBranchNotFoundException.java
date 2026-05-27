package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CrBranchNotFoundException extends BusinessException {
    public CrBranchNotFoundException(Long id) {
        super("CrBranch não encontrado com id: " + id, HttpStatus.NOT_FOUND);
    }
}
