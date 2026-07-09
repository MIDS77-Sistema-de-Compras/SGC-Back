package net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * DTO de entrada para criação e atualização de uma {@link Notification}.
 * @param title titulo da notificação, não pode ser branco e nem nulo.
 * @param message mensagem da notificação, não pode ser branco e nem nulo.
 * @param userId identificação do usuário.
 * @param requestId identificação da solicitação.
 */
public record NotificationRequest(
        @NotBlank
        String title,
        @NotBlank
        String message,
        @NotNull
        @Positive
        Long userId,
        @NotNull
        @Positive
        Long requestId
) {
}
