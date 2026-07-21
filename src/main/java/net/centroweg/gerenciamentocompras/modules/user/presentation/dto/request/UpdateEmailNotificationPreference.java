package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de requisição para atualizar a preferência de notificações por e-mail do usuário logado.
 */
public record UpdateEmailNotificationPreference(
        /**
         * Indica se o usuário deseja receber notificações por e-mail
         */
        @NotNull
        Boolean enabled
) {
}