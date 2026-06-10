package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import org.springframework.http.HttpStatus;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;

public class RequestProvisionItemNotFoundException extends BusinessException {
    public RequestProvisionItemNotFoundException(){
        super("Item de solicitação de serviço não encontrado!", HttpStatus.NOT_FOUND);
    }
}
