package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ItemRequestProductAlreadyExistsException extends BusinessException {

    public ItemRequestProductAlreadyExistsException() {
        super("Este produto já foi adicionado à solicitação.", HttpStatus.CONFLICT);
    }
}
