package net.centroweg.gerenciamentocompras.modules.provision.domain.exception;

import org.springframework.http.HttpStatus;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;

public class ProvisionNotFoundException extends BusinessException {
    public ProvisionNotFoundException(){
        super("Serviço não encontrado!", HttpStatus.NOT_FOUND);
    }
}
