package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

import java.util.List;

public interface CrBranchService {

    CrBranchResponse create(CrBranchRequest request);
    List<CrBranchResponse> findAll();
    CrBranchResponse findById(Long id);
    CrBranchResponse update(Long id, CrBranchRequest request);
    MessageDTO delete(Long id);
    List<CrBranchResponse> findCrBranchByBranch(Long branchId);
    CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId);
    CrBranchResponse removeCrBranchResponsible(Long crBranchId);
}
