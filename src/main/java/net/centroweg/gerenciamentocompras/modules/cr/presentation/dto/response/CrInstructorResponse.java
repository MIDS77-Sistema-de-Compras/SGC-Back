package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import java.util.List;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;

/**
 * DTO de saída com dados de um {@link CrInstructor}.
 * @param id identificador do CR-instrutor.
 * @param user lista com nomes dos usuários que estão associados a um CR.
 * @param crBranchId identificador da filial associada.
 */
public record CrInstructorResponse(
    Long id,
    List<UserResponse> user,
    Long crBranchId
) {}
