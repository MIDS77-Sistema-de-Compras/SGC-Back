package net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;

public record NotificationRequest(
        @NotBlank String title,
        @NotBlank String message,
        @NotNull String notificationType,
        @NotNull @Positive Long userId,
        @Positive Long requestId
) {
}
