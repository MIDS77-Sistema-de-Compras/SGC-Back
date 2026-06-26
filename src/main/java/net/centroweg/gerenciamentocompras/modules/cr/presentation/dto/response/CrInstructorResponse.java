package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import java.util.List;

import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;

public record CrInstructorResponse(
    Long id,
    List<UserResponse> user,
    Long crBranchId
) {}
