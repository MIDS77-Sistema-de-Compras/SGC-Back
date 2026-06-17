package net.centroweg.gerenciamentocompras.shared.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        LocalDateTime timestamp,
        Integer status,
        String message,
        Map<String, String> errors
) {
    public ApiError(Integer status, String message, Map<String, String> errors) {
        this(LocalDateTime.now(), status, message, errors);
    }
}
