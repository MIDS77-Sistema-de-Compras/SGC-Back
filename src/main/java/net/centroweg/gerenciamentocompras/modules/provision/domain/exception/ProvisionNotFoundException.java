package net.centroweg.gerenciamentocompras.modules.provision.domain.exception;

import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;

/**
 * Excessão lançada quando um {@link Provision} não é encontrado no banco de dados.
 */
public class ProvisionNotFoundException extends BusinessException {

    public ProvisionNotFoundException(){
        super("Serviço não encontrado!", HttpStatus.NOT_FOUND);
    }
}
