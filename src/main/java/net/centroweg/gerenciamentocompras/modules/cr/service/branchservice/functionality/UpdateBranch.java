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
 * Caso de uso responsável pela atualização dos dados de uma {@link net.centroweg.gerenciamentocompras.modules.cr.domain.Branch}.
 *
 * @author Leandro
 */
@RequiredArgsConstructor
@Service
public class UpdateBranch {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    /**
     * Atualiza o nome de uma branch existente.
     *
     * @param id            identificador da branch a ser atualizada
     * @param branchRequest DTO com os novos dados da branch
     * @return {@link BranchResponse} com os dados atualizados
     * @throws net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException se nenhuma branch for encontrada com o id informado
     */
    public BranchResponse update(Long id, BranchRequest branchRequest){
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException());
        branch.setName(branchRequest.name());
        Branch branchSalva = branchRepository.save(branch);
        BranchResponse branchResponse = branchMapper.toResponse(branchSalva);
        return branchResponse;
    }

}
