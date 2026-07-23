package net.centroweg.gerenciamentocompras.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exceção base para as regras de negócio da aplicação.
 */
@Getter
public abstract class BusinessException extends RuntimeException {

    /**
     * Código de status HTTP associado à exceção.
     */
    private final HttpStatus httpStatus;

    /**
     * Cria a exceção com a mensagem e o status HTTP informados.
     * @param message mensagem descritiva do erro.
     * @param httpStatus código de status HTTP do erro.
     */
    public BusinessException(String message,HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
