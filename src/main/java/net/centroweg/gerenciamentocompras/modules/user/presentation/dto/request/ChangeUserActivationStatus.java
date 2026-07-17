package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para alteração do estado de ativação de um usuário.
 *
 * @param active novo estado de ativação do usuário
 */
public record ChangeUserActivationStatus(
        @NotNull(message = "O estado de ativação do usuário é obrigatório")
        Boolean active
) {
}
