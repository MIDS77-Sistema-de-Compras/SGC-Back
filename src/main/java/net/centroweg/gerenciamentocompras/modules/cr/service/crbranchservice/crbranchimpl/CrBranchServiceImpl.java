package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchimpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchinterface.CrBranchService;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality.*;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrBranchServiceImpl implements CrBranchService {

    private final CreateCrBranch createCrBranch;
    private final FindAllCrBranch findAllCrBranch;
    private final FindByIdCrBranch findByIdCrBranch;
    private final UpdateCrBranch updateCrBranch;
    private final DeleteCrBranch deleteCrBranch;
    private final FindCrBranchByBranch findCrBranchByBranch;
    private final AssignCrBranchResponsible assignCrBranchResponsible;
    private final RemoveCrBranchResponsible removeCrBranchResponsible;

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
        return findByIdCrBranch.findById(id);
    }

    @Override
    public CrBranchResponse update(Long id, CrBranchRequest request) {
        return updateCrBranch.update(id, request);
    }

    @Override
    public MessageDTO delete(Long id) {
        deleteCrBranch.delete(id);
        return new MessageDTO("Deletado.");
    }

    @Override
    public List<CrBranchResponse> findCrBranchByBranch(Long branchId) {
        return findCrBranchByBranch.findCrBranchByBranch(branchId);
    }

    @Override
    public CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId) {
        return assignCrBranchResponsible.assignCrBranchResponsible(crBranchId, userId);
    }

    @Override
    public CrBranchResponse removeCrBranchResponsible(Long crBranchId) {
        return removeCrBranchResponsible.removeCrBranchResponsible(crBranchId);
    }

}
