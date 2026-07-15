package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.ApprovalDelegationStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ApprovalDelegationResponse(
        Long id,
        Long delegatorUserId,
        Long delegateUserId,
        LocalDateTime startAt,
        LocalDateTime endAt,
        ApprovalDelegationStatus status,
        LocalDateTime createdAt,
        LocalDateTime activatedAt,
        LocalDateTime finishedAt,
        List<Long> crBranchIds
) {
}
