package net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.ApprovalDelegationRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.ApprovalDelegationResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.ApprovalDelegationMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindMyApprovalDelegations {

    private final ApprovalDelegationRepository approvalDelegationRepository;
    private final UserPublicApi userPublicApi;
    private final ApprovalDelegationMapper mapper;

    @Transactional(readOnly = true)
    public List<ApprovalDelegationResponse> findAll() {
        Long currentUserId = userPublicApi.getAuthenticatedUserSummary().id();
        return approvalDelegationRepository
                .findAllByDelegatorUserIdOrderByCreatedAtDesc(currentUserId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
