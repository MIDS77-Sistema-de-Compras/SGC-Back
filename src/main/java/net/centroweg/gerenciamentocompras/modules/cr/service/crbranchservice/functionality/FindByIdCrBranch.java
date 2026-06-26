package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por buscar um vínculo entre CR e filial pelo seu identificador.
 */
@Service
@RequiredArgsConstructor
public class FindByIdCrBranch {

    private final CrBranchRepository crBranchRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Busca um vínculo CR-filial pelo seu identificador.
     *
     * @param id
     * @return o vínculo encontrado
     * @throws CrBranchNotFoundException se o vínculo não for encontrado
     */
    public CrBranchResponse findById(Long id) {
        CrBranch crBranch = crBranchRepository.findById(id)
                .orElseThrow(() -> new CrBranchNotFoundException(id));
        return crBranchMapper.toResponse(crBranch);
    }
}