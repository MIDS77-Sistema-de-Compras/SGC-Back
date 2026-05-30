package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um Centro de Resultado (CR) não é encontrado pelo identificador informado.
 * Retorna HTTP 404 ao cliente.
 */
public class CrNotFoundException extends BusinessException {

  /**
   * @param id identificador do CR que não foi encontrado
   */
  public CrNotFoundException(Long id) {
    super("CR com id " + id + " não encontrado", HttpStatus.NOT_FOUND);
  }
}
