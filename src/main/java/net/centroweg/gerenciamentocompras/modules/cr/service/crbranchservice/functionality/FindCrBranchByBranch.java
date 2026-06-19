package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso responsável por listar os vínculos CR-filial pertencentes a uma filial.
 */
@Service
@RequiredArgsConstructor
public class FindCrBranchByBranch {

    private final CrBranchRepository crBranchRepository;
    private final BranchRepository branchRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Lista todos os vínculos CR-filial associados a uma filial.
     *
     * <p>Valida previamente a existência da filial antes de realizar a busca.</p>
     *
     * @param branchId
     * @return a lista de vínculos da filial (vazia se não houver nenhum)
     * @throws BranchNotFoundException se a filial não for encontrada
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