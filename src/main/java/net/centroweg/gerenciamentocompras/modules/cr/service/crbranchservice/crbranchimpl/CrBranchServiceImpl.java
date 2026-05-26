package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchimpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality.*;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CrBranchServiceImpl {

    private final CreateCrBranch createCrBranch;
    private final FindAllCrBranch findAllCrBranch;
    private final FindByIdCrBranch findByIdCrBranch;
    private final UpdateCrBranch updateCrBranch;
    private final DeleteCrBranch deleteCrBranch;

    @Override
    public CrBranchResponse create(CrBranchRequest request) {
        return createCrBranch.create(request);
    }

    @Override
    public List<CrBranchResponse> findAll() {
        return findAllCrBranch.findAll();
    }

    @Override
    public CrBranchResponse findById(Long id) {
        return findById.findById(id);
    }

    @Override
    public CrBranchResponse update(Long id, CrBranchRequest request) {
        return updateCrBranch.update(id, request);
    }

    @Override
    public MessageDTO delete(long id) {
        deleteCrBranch.delete(id);
        return new MessageDTO("Deletado.");
    }
}
