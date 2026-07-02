package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;

/**
 * DTO de entrada para criar ou atualizar um vínculo entre CR e filial na {@link CrBranch}.
 * @param branchId identificador da filial, não deve ser nulo e nem vazio.
 * @param crId identificador do CR, não deve ser nulo e nem vazio.
 * @param responsibleUsersId identificador do usuário responsável(opcional).
 */
public record CrBranchRequest(
        @NotNull(message = "O ID da filial não deve ser nulo e nem vazio!")
        Long branchId,
        @NotNull(message = "O ID do CR não deve ser nulo e nem vazio!")
        Long crId,
        List<Long> responsibleUsersId
) {
}