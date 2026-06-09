package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrInstructorRequest(
    @NotNull(message = "O ID do supervisor não deve ser nulo.")
    @Positive(message = "O ID do supervisor não pode ser menor que 1.")
    Long instructorId,

    @NotNull(message = "O ID da filial não deve ser nulo.")
    @Positive(message = "O ID da filial não pode ser menor que 1.")
    Long crBranchId
) {}
