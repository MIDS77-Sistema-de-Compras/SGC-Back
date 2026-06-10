package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ItemRequestProductNotFoundException extends BusinessException {
    public ItemRequestProductNotFoundException() {
        super("Item De Produto não encontrado! ", HttpStatus.NOT_FOUND);
    }
}
