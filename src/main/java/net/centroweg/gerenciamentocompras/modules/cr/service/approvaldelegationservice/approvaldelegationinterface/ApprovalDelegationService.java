package net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.approvaldelegationinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CreateApprovalDelegationRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.ApprovalDelegationResponse;

import java.util.List;

public interface ApprovalDelegationService {

    ApprovalDelegationResponse create(CreateApprovalDelegationRequest request);

    List<ApprovalDelegationResponse> findMine();
}
