package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * DTO de entrada para atualização de uma {@link Request}.
 * @param crBranchId identificador da filial/CR, não pode ser nulo ou vazio.
 * @param statusName nome do status, não pode ser nulo ou vazio.
 */
public record UpdateRequestRequest(

        @NotNull(message = "O identificador do CR-filial não deve ser nulo e nem vazio!")
        Long crBranchId,

        @NotBlank(message = "O status não deve ser nulo e nem vazio!")
        String statusName
) {}
