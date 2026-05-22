package net.centroweg.gerenciamentocompras.shared.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String message,
        Map<String, String> errors
) {
    public ApiError(int status, String message, Map<String, String> errors) {
        this(LocalDateTime.now(), status, message, errors);
    }
}
