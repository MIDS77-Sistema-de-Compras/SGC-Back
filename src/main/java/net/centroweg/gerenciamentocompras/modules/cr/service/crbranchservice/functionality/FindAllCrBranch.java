package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchFilterRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.specification.CrBranchSpecifications.*;

/**
 * Caso de uso responsável por listar todos os vínculos entre CR e filial.
 */
@Service
@RequiredArgsConstructor
public class FindAllCrBranch {

    private final CrBranchRepository crBranchRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Lista todos os vínculos CR-filial cadastrados.
     *
     * @return a lista de vínculos (vazia se não houver nenhum)
     */
    @Transactional(readOnly = true)
    public List<CrBranchResponse> findAll(
            CrBranchFilterRequest filter
    ) {
        CrBranchFilterRequest safeFilter = filter != null
                ? filter
                : new CrBranchFilterRequest(null, null, Collections.emptyList());

        Specification<CrBranch> specification =
                Specification.allOf(
                        crCodeContain(safeFilter.crCode()),
                        crNameContain(safeFilter.crName()),
                        crResponsibleNameIn(safeFilter.responsibleName())
                );

        return crBranchRepository.findAll(specification)
                .stream()
                .map(crBranchMapper::toResponse)
                .toList();
    }
}
