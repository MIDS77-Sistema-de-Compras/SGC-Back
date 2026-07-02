package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um Centro de Responsabilidade(CR) não é encontrado pelo seu identificador.
 */
public class CrNotFoundException extends BusinessException {

  public CrNotFoundException(Long id) {
    super("CR com id " + id + " não encontrado", HttpStatus.NOT_FOUND);
  }
}
