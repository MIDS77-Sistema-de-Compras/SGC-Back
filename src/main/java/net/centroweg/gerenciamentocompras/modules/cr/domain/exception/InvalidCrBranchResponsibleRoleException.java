package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Lançada quando um usuário sem role elegível é informado como responsável de um CR-filial.
 */
public class InvalidCrBranchResponsibleRoleException extends BusinessException {

    public InvalidCrBranchResponsibleRoleException() {
        super(
                "Somente usuários com as roles SUPERVISOR ou COORDENADOR podem ser responsáveis por um CR-filial.",
                HttpStatus.BAD_REQUEST
        );
    }
}
