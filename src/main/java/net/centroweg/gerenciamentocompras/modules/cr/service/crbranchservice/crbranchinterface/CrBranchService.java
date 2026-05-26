package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

import java.util.List;

public interface CrBranchService {

    CrBranchResponse create(CrBranchRequest request);
    List<CrBranchResponse> findAll();
    CrBranchResponse findById(Long id);
    CrBranchResponse update(Long id, CrBranchRequest request);
    MessageDTO delete(Long id);
}
