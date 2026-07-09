package net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response;

import java.time.LocalDateTime;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * DTO de saída com os dados da {@link Notification}.
 * @param id identificador da notificação.
 * @param title titulo da notificação.
 * @param message mensagem da notificação.
 * @param viewed visualização da notificação.
 * @param createdAt criação da notificação.
 * @param userId identificador do usuário.
 * @param requestId identificador da solicitação.
 */
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
