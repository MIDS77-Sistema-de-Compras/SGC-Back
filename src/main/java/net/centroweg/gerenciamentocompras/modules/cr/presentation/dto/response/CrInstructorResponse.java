package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

public record CrInstructorResponse(
    Long id,
    User user,
    CrBranch crBranch
) {}
