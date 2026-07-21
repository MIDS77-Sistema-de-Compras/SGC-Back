package net.centroweg.gerenciamentocompras.modules.provision.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ProvisionAlreadyExistsException extends BusinessException {

    public ProvisionAlreadyExistsException() {
        super("Já existe um serviço cadastrado com esse nome.", HttpStatus.CONFLICT);
    }
}
