package net.centroweg.gerenciamentocompras.shared.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO de saída com os dados de um erro retornado pela API.
 * @param timestamp data e hora em que o erro ocorreu.
 * @param status código de status HTTP do erro.
 * @param message mensagem descritiva do erro.
 * @param errors mapa com os erros específicos de cada campo.
 */
public record ApiError(
        LocalDateTime timestamp,
        Integer status,
        String message,
        Map<String, String> errors
) {

    /**
     * Cria um erro da API definindo automaticamente a data e hora atuais.
     * @param status código de status HTTP do erro.
     * @param message mensagem descritiva do erro.
     * @param errors mapa com os erros específicos de cada campo.
     */
    public ApiError(Integer status, String message, Map<String, String> errors) {
        this(LocalDateTime.now(), status, message, errors);
    }
}
