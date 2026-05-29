package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllCrBranch {

    private final CrBranchRepository crBranchRepository;
    private final CrBranchMapper crBranchMapper;

    public List<CrBranchResponse> findAll() {
        return crBranchRepository.findAll()
                .stream()
                .map(crBranchMapper::toResponse)
                .toList();
    }
}
