package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.BranchMapper;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela remoção de uma {@link net.centroweg.gerenciamentocompras.modules.cr.domain.Branch}.
 *
 * @author Leandro
 */
@Service
@RequiredArgsConstructor
public class DeleteBranch {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    /**
     * Remove a branch com o id informado do banco de dados.
     *
     * @param id identificador da branch a ser removida
     * @return {@link MessageDTO} com a confirmação da operação
     */
    public MessageDTO delete(Long id){
        branchRepository.deleteById(id);
        return new MessageDTO("Error");
    }
}
