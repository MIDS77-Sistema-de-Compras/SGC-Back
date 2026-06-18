package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveCrBranchResponsible {

    private final CrBranchRepository crBranchRepository;
    private final CrBranchMapper crBranchMapper;

    public CrBranchResponse removeCrBranchResponsible(Long crBranchId) {
        CrBranch crBranch = crBranchRepository.findById(crBranchId)
                .orElseThrow(() -> new CrBranchNotFoundException(crBranchId));

        crBranch.setResponsibleUser(null);
        crBranchRepository.save(crBranch);
        return crBranchMapper.toResponse(crBranch);
    }

}
