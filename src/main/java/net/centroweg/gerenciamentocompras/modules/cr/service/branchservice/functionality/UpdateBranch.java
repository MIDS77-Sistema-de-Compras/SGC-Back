package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.BranchMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela atualização dos dados de uma {@link Branch}.
 */
@RequiredArgsConstructor
@Service
public class UpdateBranch {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    /**
     * Atualiza uma filial existente no banco de dados.
     * @param id identificador da filial.
     * @param branchRequest novos dados da filial.
     * @return filial já atualizada.
     * @throws BranchNotFoundException se nenhuma filial com o ID informado for encontrada.
     */
    public BranchResponse update(Long id, BranchRequest branchRequest){
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException());
        branch.setName(branchRequest.name());
        Branch branchSalva = branchRepository.save(branch);
        BranchResponse branchResponse = branchMapper.toResponse(branchSalva);
        return branchResponse;
    }

}
