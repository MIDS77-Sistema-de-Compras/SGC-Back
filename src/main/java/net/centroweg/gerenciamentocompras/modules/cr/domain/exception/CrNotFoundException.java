package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CrNotFoundException extends BusinessException {
  public CrNotFoundException(Long id) {
    super("CR com id " + id + " não encontrado", HttpStatus.NOT_FOUND);
  }
}
