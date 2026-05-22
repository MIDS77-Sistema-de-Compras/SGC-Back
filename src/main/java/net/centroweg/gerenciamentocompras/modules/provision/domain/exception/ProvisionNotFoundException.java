package net.centroweg.gerenciamentocompras.modules.provision.domain.exception;

public class ProvisionNotFoundException extends RuntimeException {
    public ProvisionNotFoundException(String message){
        super(message);
    }
}
