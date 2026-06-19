package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.apache.hc.core5.http.HttpStatus;

public class AcessDeniedException extends BusinessException {
    public AcessDeniedException (){
        super("Acesso negado por questões de segurança", org.springframework.http.HttpStatus.valueOf(HttpStatus.SC_NOT_ACCEPTABLE));
    }
}
