package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RoleNotAllowedException extends BusinessException {
    public RoleNotAllowedException(){
        super("Houve um erro pois não é possível criar um user com esta role", HttpStatus.FORBIDDEN);
    }
}
