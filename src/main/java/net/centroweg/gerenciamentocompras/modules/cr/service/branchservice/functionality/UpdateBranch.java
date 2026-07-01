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
     * Atualiza os dados de uma branch existente.
     * @param id identificador da branch a ser atualizada.
     * @param branchRequest novos dados da branch.
     * @return a branch com os dados atualizados.
     * @throws BranchNotFoundException se nenhuma branch for encontrada com o ID informado.
     */
    public BranchResponse update(Long id, BranchRequest branchRequest){
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException());
        branch.setName(branchRequest.name());
        Branch branchSalva = branchRepository.save(branch);
        BranchResponse branchResponse = branchMapper.toResponse(branchSalva);
        return branchResponse;
    }

}
