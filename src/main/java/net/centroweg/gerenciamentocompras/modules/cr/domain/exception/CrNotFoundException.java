package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

public class CrNotFoundException extends RuntimeException {
  public CrNotFoundException(String message) {
    super(message);
  }
}
