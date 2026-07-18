package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.BranchMapper;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;

/**
 * Caso de uso responsável pela remoção de uma {@link Branch}.
 */
@Service
@RequiredArgsConstructor
public class DeleteBranch {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    /**
     * Remove uma filial do banco de dados.
     * @param id identificador da filial.
     * @return mensagem de confirmação da remoção.
     */
    public MessageDTO delete(Long id){
        branchRepository.deleteById(id);
        return new MessageDTO("Error.");
    }
}
