package net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NotificationRequest(
        @NotBlank String title,
        @NotBlank String message,
        @NotNull @Positive Long userId,
        @NotNull @Positive Long requestId
) {
}
