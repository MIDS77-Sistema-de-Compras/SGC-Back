package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.BranchMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela criação de uma nova {@link Branch}.
 */
@RequiredArgsConstructor
@Service
public class CreateBranch {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    /**
     * Cria e persiste uma nova filial no banco de dados.
     * @param branchRequest dados da filial a ser criada.
     * @return filial criada.
     */
    public BranchResponse create(BranchRequest branchRequest){
        Branch branch = branchMapper.toEntity(branchRequest);
        Branch branchSaved = branchRepository.save(branch);
        BranchResponse branchResponse = branchMapper.toResponse(branchSaved);
        return branchResponse;
    }
}
