package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrEditNotAllowedException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por validar se o usuário autenticado pode editar CRs.
 *
 * <p>A edição de CR é permitida apenas ao coordenador do CR Master
 * (usuário com role COORDENADOR responsável por um vínculo cujo CR é master)
 * e ao administrador do sistema.</p>
 */
@Service
@RequiredArgsConstructor
public class ValidateCrMasterCoordinator {

    private final CurrentUserService currentUserService;
    private final CrBranchRepository crBranchRepository;

    /**
     * Valida se o usuário autenticado pode editar CRs.
     *
     * @throws CrEditNotAllowedException se o usuário não for coordenador do CR Master nem administrador
     */
    public void validate() {
        User currentUser = currentUserService.getCurrentUser();

        if (hasRole(currentUser, Authorities.ADMIN)) {
            return;
        }

        boolean isMasterCoordinator = hasRole(currentUser, Authorities.COORDENADOR)
                && crBranchRepository.existsByCrMasterTrueAndResponsibleUsersId(currentUser.getId());

        if (!isMasterCoordinator) {
            throw new CrEditNotAllowedException();
        }
    }

    private boolean hasRole(User user, String roleName) {
        return user.getRole() != null
                && user.getRole().getName() != null
                && user.getRole().getName().trim().equalsIgnoreCase(roleName);
    }
}
