package net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * DTO de entrada para criação e atualização de uma {@link Notification}.
 * @param title titulo da notificação, não pode ser nulo ou vazio.
 * @param message mensagem da notificação, não pode ser nula ou vazia.
 * @param userId identificador do usuário, não pode ser nulo ou vazio e deve ser positivo.
 * @param requestId identificador da solicitação, não pode ser nulo ou vazio e deve ser positivo.
 */
public record NotificationRequest(
        @NotBlank(message = "O título da notificação não deve ser nulo e nem vazio!")
        String title,
        @NotBlank(message = "A mensagem da notificação não deve ser nula e nem vazia!")
        String message,
        @NotNull(message = "O identificador do usuário não deve ser nulo e nem vazio!")
        @Positive(message = "O identificador do usuário deve ser positivo!")
        Long userId,
        @NotNull(message = "O identificador da solicitação não deve ser nulo e nem vazio!")
        @Positive(message = "O identificador da solicitação deve ser positivo!")
        Long requestId
) {
}
