package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;

public record CrInstructorResponse(
    Long id,
    UserResponse user,
    Long crBranchId
) {}
