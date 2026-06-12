package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.BranchMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FindByIdBranch {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public BranchResponse findById(Long id){
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException());
        BranchResponse branchResponse = branchMapper.toResponse(branch);
        return branchResponse;
    }
}
