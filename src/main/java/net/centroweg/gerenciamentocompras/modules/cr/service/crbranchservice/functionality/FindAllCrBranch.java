package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchFilterRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.specification.CrBranchSpecifications.*;

@Service
@RequiredArgsConstructor
public class FindAllCrBranch {

    private final CrBranchRepository crBranchRepository;
    private final CrBranchMapper crBranchMapper;

    public List<CrBranchResponse> findAll(
            CrBranchFilterRequest filter
    ) {
        Specification<CrBranch> specification =
                Specification.allOf(
                        crCodeContain(filter.crCode()),
                        crNameContain(filter.crName()),
                        crResponsibleNameContain(filter.responsibleName())
                );

        return crBranchRepository.findAll(specification)
                .stream()
                .map(crBranchMapper::toResponse)
                .toList();
    }
}
