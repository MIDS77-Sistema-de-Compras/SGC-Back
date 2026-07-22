package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * DTO de entrada para atualização do status de uma {@link Request}.
 * @param statusName nome do status, não pode ser nulo ou vazio.
 * @param justification justificativa da alteração de status.
 */
public record UpdateRequestStatus(

        @NotBlank(message = "O status não deve ser nulo e nem vazio!")
        String statusName,

        String justification
) {
}