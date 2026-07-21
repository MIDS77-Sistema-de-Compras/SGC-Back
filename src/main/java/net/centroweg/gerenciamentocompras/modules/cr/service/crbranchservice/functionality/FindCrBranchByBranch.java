package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;

/**
 * Caso de uso responsável por listar os vínculos {@link CrBranch} pertencentes a filial informada.
 */
@Service
@RequiredArgsConstructor
public class FindCrBranchByBranch {

    private final CrBranchRepository crBranchRepository;
    private final BranchRepository branchRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Lista todos os vínculos CR-filial pertencentes a filial informada cadastrados no banco de dados.
     * @param branchId identificador da filial.
     * @return lista com todos os vínculos da filial, caso exista.
     * @throws BranchNotFoundException se a filial não for encontrada.
     */
    public List<CrBranchResponse> findCrBranchByBranch(Long branchId) {
        branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException());

        return crBranchRepository.findByBranchId(branchId)
                .stream()
                .map(crBranchMapper::toResponse)
                .toList();
    }

}