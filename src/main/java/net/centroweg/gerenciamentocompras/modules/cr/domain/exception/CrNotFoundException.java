package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

public class CrNotFoundException extends RuntimeException {
  public CrNotFoundException(Long id) {
    super("CR com id " + id + " não encontrado");
  }
}
