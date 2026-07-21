package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por buscar um vínculo {@link CrBranch} pelo seu identificador.
 */
@Service
@RequiredArgsConstructor
public class FindByIdCrBranch {

    private final CrBranchRepository crBranchRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Busca um vínculo CR-filial no banco de dados pelo ID informado.
     * @param id identificador do vínculo.
     * @return vínculo encontrado, caso exista.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     */
    public CrBranchResponse findById(Long id) {
        CrBranch crBranch = crBranchRepository.findById(id)
                .orElseThrow(() -> new CrBranchNotFoundException(id));
        return crBranchMapper.toResponse(crBranch);
    }
}