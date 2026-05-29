package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record CrBranchRequest(
        @NotNull Long branchId,
        @NotNull Long crId,
        Long responsibleUserId
) {
}
