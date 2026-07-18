package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;

/**
 * DTO de entrada para criar um vínculo {@link CrInstructor}.
 * @param instructorIds lista de identificadores dos usuários, não deve ser nula ou vazia e o identificador deve ser positivo.
 * @param crBranchId identificador do CR-filial, não deve ser nulo ou vazio e deve ser positivo.
 */
public record CrInstructorRequest(
    @NotNull(message = "O ID do usuário não deve ser nulo e nem vazio.")
    List<@Positive(message = "O ID do usuário deve ser maior ou igual a zero.") Long> instructorIds,

    @NotNull(message = "O ID do CR-filial não deve ser nulo e nem vazio.")
    @Positive(message = "O ID do CR-filial deve ser maior ou igual a zero.")
    Long crBranchId
) {}
