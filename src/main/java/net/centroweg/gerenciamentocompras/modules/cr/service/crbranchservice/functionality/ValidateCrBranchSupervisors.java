package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.MaxActiveSupervisorsException;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso responsável por validar o limite de supervisores ativos
 * vinculados como responsáveis de um CR-filial.
 *
 * <p>Um CR pode ter no máximo {@value #MAX_ACTIVE_SUPERVISORS} supervisores
 * ativos como responsáveis. Usuários inativos ou de outras roles não contam
 * para o limite.</p>
 */
@Service
public class ValidateCrBranchSupervisors {

    private static final int MAX_ACTIVE_SUPERVISORS = 2;

    /**
     * Valida a lista de responsáveis de um CR-filial.
     *
     * @param responsibleUsers lista de responsáveis a validar (pode ser nula)
     * @throws MaxActiveSupervisorsException se houver mais supervisores ativos que o permitido
     */
    public void validate(List<User> responsibleUsers) {
        if (responsibleUsers == null) {
            return;
        }

        long activeSupervisors = responsibleUsers.stream()
                .filter(this::isActiveSupervisor)
                .count();

        if (activeSupervisors > MAX_ACTIVE_SUPERVISORS) {
            throw new MaxActiveSupervisorsException(MAX_ACTIVE_SUPERVISORS);
        }
    }

    private boolean isActiveSupervisor(User user) {
        return Boolean.TRUE.equals(user.getActive())
                && user.getRole() != null
                && user.getRole().getName() != null
                && user.getRole().getName().trim().equalsIgnoreCase(Authorities.SUPERVISOR);
    }
}
