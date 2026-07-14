package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AcessDeniedException extends BusinessException {
    public AcessDeniedException() {
        super("Acesso negado por questoes de seguranca", HttpStatus.FORBIDDEN);
    }
}
