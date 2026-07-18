package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.BranchMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso responsável pela listagem de todas as {@link Branch} cadastradas.
 */
@RequiredArgsConstructor
@Service
public class findAllBranch {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    /**
     * Retorna todas as filiais cadastradas no banco de dados.
     * @return lista todas as filiais encontradas, caso exista.
     */
    public List<BranchResponse> listAll(){
        List<Branch> branches = branchRepository.findAll();
        List<BranchResponse> dto = new ArrayList<>();

        for(Branch b: branches){
             dto.add(branchMapper.toResponse(b));
        }
        return dto;
    }
}
