package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Caso de uso responsável por adicionar um usuário responsável a um vínculo CR-filial.
 *
 * <p>O usuário informado é adicionado à lista de responsáveis existente,
 * sem substituir os responsáveis já vinculados.</p>
 */
@Service
@RequiredArgsConstructor
public class AssignCrBranchResponsible {

    private final CrBranchRepository crBranchRepository;
    private final UserPublicApi userPublicApi;
    private final CrBranchMapper crBranchMapper;
    private final ValidateCrBranchSupervisors validateCrBranchSupervisors;

    /**
     * Adiciona um usuário à lista de responsáveis de um vínculo CR-filial.
     *
     * <p>Caso o usuário já seja responsável pelo vínculo, nada é alterado.</p>
     *
     * @param crBranchId
     * @param userId
     * @return o vínculo atualizado com o responsável adicionado
     * @throws CrBranchNotFoundException se o vínculo não for encontrado
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    public CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId) {
        CrBranch crBranch = crBranchRepository.findById(crBranchId)
                .orElseThrow(() -> new CrBranchNotFoundException(crBranchId));

        User user = userPublicApi.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o ID: " + userId));

        if (crBranch.getResponsibleUsers() == null) {
            crBranch.setResponsibleUsers(new ArrayList<>());
        }

        boolean alreadyResponsible = crBranch.getResponsibleUsers().stream()
                .anyMatch(responsible -> responsible.getId().equals(userId));

        if (!alreadyResponsible) {
            crBranch.getResponsibleUsers().add(user);
            validateCrBranchSupervisors.validate(crBranch.getResponsibleUsers());
            crBranchRepository.save(crBranch);
        }

        return crBranchMapper.toResponse(crBranch);
    }
}
