package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.BranchMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela criação de uma nova {@link net.centroweg.gerenciamentocompras.modules.cr.domain.Branch}.
 *
 * @author Leandro
 */
@RequiredArgsConstructor
@Service
public class CreateBranch {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    /**
     * Cria e persiste uma nova branch no banco de dados.
     *
     * @param branchRequest DTO com os dados da branch a ser criada
     * @return {@link BranchResponse} com os dados da branch criada, incluindo o id gerado
     */
    public BranchResponse create(BranchRequest branchRequest){
        Branch branch = branchMapper.toEntity(branchRequest);
        Branch branchSaved = branchRepository.save(branch);
        BranchResponse branchResponse = branchMapper.toResponse(branchSaved);
        return branchResponse;
    }
}
