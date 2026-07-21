package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por remover um usuário responsável de um vínculo {@link CrBranch}.
 */
@Service
@RequiredArgsConstructor
public class RemoveCrBranchResponsible {

    private final CrBranchRepository crBranchRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Remove um usuário responsável de um vínculo CR-filial do banco de dados.
     * @param crBranchId identificador do vínculo.
     * @return vínculo já atualizado.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     */
    public CrBranchResponse removeCrBranchResponsible(Long crBranchId) {
        CrBranch crBranch = crBranchRepository.findById(crBranchId)
                .orElseThrow(() -> new CrBranchNotFoundException(crBranchId));

        crBranch.setResponsibleUsers(null);
        crBranchRepository.save(crBranch);
        return crBranchMapper.toResponse(crBranch);
    }

}