package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por remover um usuário responsável de um vínculo CR-filial.
 */
@Service
@RequiredArgsConstructor
public class RemoveCrBranchResponsible {

    private final CrBranchRepository crBranchRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Remove um usuário específico da lista de responsáveis de um vínculo CR-filial.
     *
     * <p>Os demais responsáveis do vínculo são mantidos.</p>
     *
     * @param crBranchId
     * @param userId
     * @return o vínculo atualizado sem o responsável removido
     * @throws CrBranchNotFoundException se o vínculo não for encontrado
     */
    public CrBranchResponse removeCrBranchResponsible(Long crBranchId, Long userId) {
        CrBranch crBranch = crBranchRepository.findById(crBranchId)
                .orElseThrow(() -> new CrBranchNotFoundException(crBranchId));

        if (crBranch.getResponsibleUsers() != null) {
            boolean removed = crBranch.getResponsibleUsers()
                    .removeIf(responsible -> responsible.getId().equals(userId));

            if (removed) {
                crBranchRepository.save(crBranch);
            }
        }

        return crBranchMapper.toResponse(crBranch);
    }

}