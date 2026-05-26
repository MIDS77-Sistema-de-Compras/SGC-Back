package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

public class CrBranchNotFoundException extends RuntimeException {
  public CrBranchNotFoundException(String message) {
    super(message);
  }
}
