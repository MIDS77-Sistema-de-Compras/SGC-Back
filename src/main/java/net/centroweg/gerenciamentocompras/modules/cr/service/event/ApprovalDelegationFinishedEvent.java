package net.centroweg.gerenciamentocompras.modules.cr.service.event;

import java.time.LocalDateTime;
import java.util.List;

public record ApprovalDelegationFinishedEvent(
        Long delegationId,
        Long delegatorUserId,
        Long delegateUserId,
        List<Long> branchIds,
        LocalDateTime finishedAt
) {
}
