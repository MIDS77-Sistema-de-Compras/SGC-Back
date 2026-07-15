package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SupervisorWithoutCrBranchException extends BusinessException {

    public SupervisorWithoutCrBranchException() {
        super("O supervisor titular não é responsável por nenhuma filial.", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
