package net.centroweg.gerenciamentocompras.modules.user.service.authorization;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotAllowedException;
import net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels.SystemRole;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleAuthorizationService {

    private final CurrentUserService currentUserService;

    public void validateCanCreate(SystemRole targetRole) {
        validateCanCreate(currentActorRole(), targetRole);
    }

    public void validateCanCreate(SystemRole actorRole, SystemRole targetRole) {
        validateAllowed(canCreate(actorRole, targetRole));
    }

    public void validateCanEdit(SystemRole targetRole) {
        validateCanEdit(currentActorRole(), targetRole);
    }

    public void validateCanEdit(SystemRole actorRole, SystemRole targetRole) {
        validateAllowed(canEdit(actorRole, targetRole));
    }

    public void validateCanDeactivate(SystemRole targetRole) {
        validateCanDeactivate(currentActorRole(), targetRole);
    }

    public void validateCanDeactivate(SystemRole actorRole, SystemRole targetRole) {
        validateAllowed(canDeactivate(actorRole, targetRole));
    }

    private boolean canCreate(SystemRole actorRole, SystemRole targetRole) {
        if (actorRole == null || targetRole == null) {
            return false;
        }

        return switch (actorRole) {
            case SUPERVISOR -> targetRole == SystemRole.DOCENTE;
            case COORDENADOR -> targetRole == SystemRole.DOCENTE
                    || targetRole == SystemRole.SUPERVISOR;
            case ADMIN -> targetRole == SystemRole.DOCENTE
                    || targetRole == SystemRole.SUPERVISOR
                    || targetRole == SystemRole.COORDENADOR
                    || targetRole == SystemRole.COMPRADOR;
            default -> false;
        };
    }

    private boolean canEdit(SystemRole actorRole, SystemRole targetRole) {
        if (actorRole == null || targetRole == null) {
            return false;
        }

        return switch (actorRole) {
            case SUPERVISOR -> targetRole == SystemRole.DOCENTE;
            case COORDENADOR -> targetRole == SystemRole.DOCENTE
                    || targetRole == SystemRole.SUPERVISOR;
            case ADMIN -> true;
            default -> false;
        };
    }

    private boolean canDeactivate(SystemRole actorRole, SystemRole targetRole) {
        if (actorRole == null || targetRole == null) {
            return false;
        }

        return switch (actorRole) {
            case SUPERVISOR -> targetRole == SystemRole.DOCENTE;
            case COORDENADOR -> targetRole == SystemRole.DOCENTE
                    || targetRole == SystemRole.SUPERVISOR;
            case ADMIN -> true;
            default -> false;
        };
    }

    private SystemRole currentActorRole() {
        User currentUser = currentUserService.getCurrentUser();
        String roleName = currentUser.getRole() != null
                ? currentUser.getRole().getName()
                : null;

        return SystemRole.from(roleName);
    }

    private void validateAllowed(boolean allowed) {
        if (!allowed) {
            throw new RoleNotAllowedException();
        }
    }
}
