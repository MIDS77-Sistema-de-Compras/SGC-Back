package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindCrBranchByBranch {

    private final CrBranchRepository crBranchRepository;
    private final BranchRepository branchRepository;
    private final CrBranchMapper crBranchMapper;

    public List<CrBranchResponse> findCrBranchByBranch(Long branchId) {
        branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException());

        return crBranchRepository.findByBranchId(branchId)
                .stream()
                .map(crBranchMapper::toResponse)
                .toList();
    }

}
