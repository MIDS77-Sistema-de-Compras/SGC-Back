package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import java.util.List;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;

/**
 * DTO de saída com dados do {@link CrInstructor}.
 * @param id identificador único do CR-Instructor.
 * @param user lista com nomes dos usuários que estão associados a uma CR.
 * @param crBranchId identificador único da branch associada.
 */
public record CrInstructorResponse(
    Long id,
    List<UserResponse> user,
    Long crBranchId
) {}
