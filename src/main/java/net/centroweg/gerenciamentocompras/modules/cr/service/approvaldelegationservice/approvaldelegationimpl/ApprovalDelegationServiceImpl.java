package net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.approvaldelegationimpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CreateApprovalDelegationRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.ApprovalDelegationResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.approvaldelegationinterface.ApprovalDelegationService;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.CreateApprovalDelegation;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.FindMyApprovalDelegations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalDelegationServiceImpl implements ApprovalDelegationService {

    private final CreateApprovalDelegation createApprovalDelegation;
    private final FindMyApprovalDelegations findMyApprovalDelegations;

    @Override
    public ApprovalDelegationResponse create(CreateApprovalDelegationRequest request) {
        return createApprovalDelegation.create(request);
    }

    @Override
    public List<ApprovalDelegationResponse> findMine() {
        return findMyApprovalDelegations.findAll();
    }
}
