package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.ApprovalDelegation;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.ApprovalDelegationResponse;
import org.springframework.stereotype.Component;

@Component
public class ApprovalDelegationMapper {

    public ApprovalDelegationResponse toResponse(ApprovalDelegation delegation) {
        return new ApprovalDelegationResponse(
                delegation.getId(),
                delegation.getDelegatorUserId(),
                delegation.getDelegateUserId(),
                delegation.getStartAt(),
                delegation.getEndAt(),
                delegation.getStatus(),
                delegation.getCreatedAt(),
                delegation.getActivatedAt(),
                delegation.getFinishedAt(),
                delegation.getBranches().stream()
                        .map(branch -> branch.getCrBranch().getId())
                        .sorted()
                        .toList()
        );
    }
}
