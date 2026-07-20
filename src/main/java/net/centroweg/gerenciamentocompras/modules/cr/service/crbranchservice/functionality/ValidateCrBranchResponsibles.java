package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.InvalidCrBranchResponsibleRoleException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.MaxActiveSupervisorsException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.MaxCrBranchCoordinatorsException;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso responsável por validar as roles e os limites dos responsáveis de um CR-filial.
 */
@Service
public class ValidateCrBranchResponsibles {

    private static final int MAX_ACTIVE_SUPERVISORS = 2;
    private static final int MAX_COORDINATORS = 1;

    /**
     * Valida a lista completa de responsáveis antes de ela ser persistida.
     *
     * @param responsibleUsers responsáveis do CR-filial, podendo ser nula ou vazia
     */
    public void validate(List<User> responsibleUsers) {
        if (responsibleUsers == null || responsibleUsers.isEmpty()) {
            return;
        }

        responsibleUsers.forEach(this::validateAllowedRole);

        long activeSupervisors = responsibleUsers.stream()
                .filter(this::isActiveSupervisor)
                .count();
        if (activeSupervisors > MAX_ACTIVE_SUPERVISORS) {
            throw new MaxActiveSupervisorsException(MAX_ACTIVE_SUPERVISORS);
        }

        long coordinators = responsibleUsers.stream()
                .filter(this::isCoordinator)
                .count();
        if (coordinators > MAX_COORDINATORS) {
            throw new MaxCrBranchCoordinatorsException(MAX_COORDINATORS);
        }
    }

    private void validateAllowedRole(User user) {
        if (user == null || user.getRole() == null || !hasRoleName(user)) {
            throw new InvalidCrBranchResponsibleRoleException();
        }

        if (!isSupervisor(user) && !isCoordinator(user)) {
            throw new InvalidCrBranchResponsibleRoleException();
        }
    }

    private boolean isActiveSupervisor(User user) {
        return Boolean.TRUE.equals(user.getActive()) && isSupervisor(user);
    }

    private boolean isSupervisor(User user) {
        return hasRole(user, Authorities.SUPERVISOR);
    }

    private boolean isCoordinator(User user) {
        return hasRole(user, Authorities.COORDENADOR);
    }

    private boolean hasRole(User user, String roleName) {
        return user != null
                && user.getRole() != null
                && hasRoleName(user)
                && user.getRole().getName().trim().equalsIgnoreCase(roleName);
    }

    private boolean hasRoleName(User user) {
        String roleName = user.getRole().getName();
        return roleName != null && !roleName.trim().isEmpty();
    }
}
