package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Caso de uso responsável por atribuir um usuário responsável a um vínculo CR-filial.
 */
@Service
@RequiredArgsConstructor
public class AssignCrBranchResponsible {

    private final CrBranchRepository crBranchRepository;
    private final UserRepository userRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Atribui um usuário responsável por um vínculo CR-filial no banco de dados.
     * @param crBranchId identificador da CR-filial.
     * @param userId identificador do usuário.
     * @return vínculo atualizado.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    public CrBranchResponse assignCrBranchResponsible(Long crBranchId, List<Long> userId) {
        CrBranch crBranch = crBranchRepository.findById(crBranchId)
                .orElseThrow(() -> new CrBranchNotFoundException(crBranchId));

        if (crBranch.getResponsibleUsers() != null && !crBranch.getResponsibleUsers().isEmpty()) {
            crBranch.setResponsibleUsers(null);
        }

        List<User> users = userRepository.findAllById(userId);

        crBranch.setResponsibleUsers(users);
        crBranchRepository.save(crBranch);
        return crBranchMapper.toResponse(crBranch);
    }
}
