package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.BranchMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateBranch {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public BranchResponse create(BranchRequest branchRequest){
        Branch branch = branchMapper.toEntity(branchRequest);
        Branch branchSaved = branchRepository.save(branch);
        BranchResponse branchResponse = branchMapper.toResponse(branchSaved);
        return branchResponse;
    }
}
