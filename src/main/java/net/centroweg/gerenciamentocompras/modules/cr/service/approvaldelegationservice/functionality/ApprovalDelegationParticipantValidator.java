package net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality;

import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.InvalidApprovalDelegationParticipantException;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserSummaryPublicResponse;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.springframework.stereotype.Component;

@Component
public class ApprovalDelegationParticipantValidator {

    public void validateActiveSupervisor(UserSummaryPublicResponse user, String participantName) {
        if (!Authorities.SUPERVISOR.equalsIgnoreCase(user.role())) {
            throw new InvalidApprovalDelegationParticipantException(
                    "O " + participantName + " deve possuir a função SUPERVISOR."
            );
        }
        if (!user.active()) {
            throw new InvalidApprovalDelegationParticipantException(
                    "O " + participantName + " deve estar ativo."
            );
        }
    }
}
