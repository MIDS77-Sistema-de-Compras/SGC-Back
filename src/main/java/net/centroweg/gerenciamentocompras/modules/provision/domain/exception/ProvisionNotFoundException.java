package net.centroweg.gerenciamentocompras.modules.provision.domain.exception;

import org.springframework.http.HttpStatus;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;

/**
 * Classe detendo a exceção disparada caso o serviço especificado não seja encontrado.
 */
public class ProvisionNotFoundException extends BusinessException {
    /**
     * Recebe tanto a mensagem, quanto o status HTTP 404 (Not Found).
     */
    public ProvisionNotFoundException(){
        super("Serviço não encontrado!", HttpStatus.NOT_FOUND);
    }
}
