package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class BranchNotFoundException extends BusinessException {
    public BranchNotFoundException(){
        super("Branch não encontrada", HttpStatus.NOT_FOUND);
    }
}
