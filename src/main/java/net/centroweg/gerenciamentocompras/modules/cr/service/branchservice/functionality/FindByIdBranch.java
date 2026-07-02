package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.BranchMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela busca de uma {@link Branch} pelo seu identificador.
 */
@RequiredArgsConstructor
@Service
public class FindByIdBranch {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    /**
     * Busca uma filial pelo ID informado.
     * @param id identificador da filial.
     * @return dados da filial encontrada.
     * @throws BranchNotFoundException se nenhuma filial for encontrada com o ID informado.
     */
    public BranchResponse findById(Long id){
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException());
        BranchResponse branchResponse = branchMapper.toResponse(branch);
        return branchResponse;
    }
}
