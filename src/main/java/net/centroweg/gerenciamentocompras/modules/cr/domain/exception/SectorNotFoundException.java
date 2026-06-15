package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SectorNotFoundException extends BusinessException {
    public SectorNotFoundException() {
        super("Bloco não encontrado!", HttpStatus.NOT_FOUND);
    }
}
