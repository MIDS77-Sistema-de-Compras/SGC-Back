package net.centroweg.gerenciamentocompras.modules.provision.domain.exception;

public class ProvisionNotFoundException extends BusinessException {
    public ProvisionNotFoundException(String message){
        super(message);
    }
}
