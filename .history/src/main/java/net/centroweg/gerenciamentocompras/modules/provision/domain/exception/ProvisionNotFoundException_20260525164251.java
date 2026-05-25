package net.centroweg.gerenciamentocompras.modules.provision.domain.exception;

import net.centroweg.gerenciamentocompras.modules.shared.exception.BusinessException;

public class ProvisionNotFoundException extends BusinessException {
    public ProvisionNotFoundException(String message){
        super(message);
    }
}
