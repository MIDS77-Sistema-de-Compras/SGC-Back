package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.branchinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

import java.util.List;

public interface BranchService {

    BranchResponse create(BranchRequest branchRequest);
    List<BranchResponse> findAll();
    BranchResponse update(long id, BranchRequest branchRequest);
    BranchResponse findById(long id);
    MessageDTO delete(long id);
}
