package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrInstructorRequest(
    @NotNull(message = "O ID do professor não deve ser nulo.")
    List<@Positive(message = "O ID do professor não pode ser menor que 1.") Long> instructorIds,

    @NotNull(message = "O ID da filial não deve ser nulo.")
    @Positive(message = "O ID da filial não pode ser menor que 1.")
    Long crBranchId
) {}
