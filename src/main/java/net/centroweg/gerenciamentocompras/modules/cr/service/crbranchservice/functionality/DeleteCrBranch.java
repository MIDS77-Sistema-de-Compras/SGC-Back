package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por remover um vínculo {@link CrBranch}.
 */
@Service
@RequiredArgsConstructor
public class DeleteCrBranch {

    private final CrBranchRepository crBranchRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Remove um vínculo CR-filial do banco de dados.
     * @param id identificador do vínculo.
     * @return mensagem de confirmação da remoção.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     */
    public MessageDTO delete(Long id) {
        CrBranch crBranch = crBranchRepository.findById(id)
                .orElseThrow(() -> new CrBranchNotFoundException(id));
        crBranchRepository.delete(crBranch);
        return new MessageDTO("Deletado!");
    }
}
