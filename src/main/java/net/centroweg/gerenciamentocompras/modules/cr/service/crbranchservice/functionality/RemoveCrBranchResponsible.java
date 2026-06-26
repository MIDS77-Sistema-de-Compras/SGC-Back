package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por remover o usuário responsável de um vínculo CR-filial.
 */
@Service
@RequiredArgsConstructor
public class RemoveCrBranchResponsible {

    private final CrBranchRepository crBranchRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Remove o usuário responsável de um vínculo CR-filial, deixando o campo nulo.
     *
     * @param crBranchId
     * @return o vínculo atualizado sem responsável
     * @throws CrBranchNotFoundException se o vínculo não for encontrado
     */
    public CrBranchResponse removeCrBranchResponsible(Long crBranchId) {
        CrBranch crBranch = crBranchRepository.findById(crBranchId)
                .orElseThrow(() -> new CrBranchNotFoundException(crBranchId));

        crBranch.setResponsibleUsers(null);
        crBranchRepository.save(crBranch);
        return crBranchMapper.toResponse(crBranch);
    }

}