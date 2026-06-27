package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Excessão lançada quando um Sector não é encontrado pelo seu identificador.
 */
public class SectorNotFoundException extends BusinessException {
    public SectorNotFoundException() {
        super("Bloco não encontrado!", HttpStatus.NOT_FOUND);
    }
}
