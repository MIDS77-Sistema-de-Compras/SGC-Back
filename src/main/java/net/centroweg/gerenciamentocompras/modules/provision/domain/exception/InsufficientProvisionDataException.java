package net.centroweg.gerenciamentocompras.modules.provision.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InsufficientProvisionDataException extends BusinessException {
    public InsufficientProvisionDataException() {
        super("Dados insuficientes para criar o servi\u00E7o.", HttpStatus.BAD_REQUEST);
    }
}