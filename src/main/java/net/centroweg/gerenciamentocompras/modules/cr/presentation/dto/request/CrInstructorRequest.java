package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;

/**
 * DTO de entrada para criar um vínculo entre CR e usuário na {@link CrInstructor}.
 * @param instructorIds lista de identificadores dos usuários, não deve ser nula e nem vazia e o identificador deve ser positivo.
 * @param crBranchId identificador do CR-filial, não deve ser nulo e nem vazio, devendo ser positivo.
 */
public record CrInstructorRequest(
    @NotNull(message = "O ID do usuário não deve ser nulo.")
    List<@Positive(message = "O ID do usuário não pode ser menor que 1.") Long> instructorIds,

    @NotNull(message = "O ID da CR-filial não deve ser nulo.")
    @Positive(message = "O ID da CR-filial não pode ser menor que 1.")
    Long crBranchId
) {}
