package net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
        @NotBlank String title,
        @NotBlank String message,
        @NotNull Long userId,
        Long requestId
) {
}
