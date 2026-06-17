package net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String title,
        String message,
        Boolean viewed,
        LocalDateTime createdAt,
        Long userId,
        Long requestId
) {
}
