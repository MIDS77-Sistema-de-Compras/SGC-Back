package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

public class StatusNotFoundException extends RuntimeException {

    public StatusNotFoundException() {
        super("Status não encontrado!");
    }
}
