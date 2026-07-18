package net.centroweg.gerenciamentocompras.modules.request.service.validator;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.*;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class RequestBusinessRuleValidator {

    private static final Set<String> SUPERVISOR_APPROVED_OR_AFTER = Set.of(
            "aprovado",
            "auto_aprovado",
            "em atendimento",
            "entregue",
            "cancelado"
    );

    private static final Set<String> OPERATIONAL_APPROVED_OR_AFTER = Set.of(
            "em atendimento",
            "entregue",
            "cancelado"
    );

    /**
     * Único status em que o supervisor/coordenador ainda pode decidir a solicitação.
     * Qualquer outro — Aprovado, Recusado, Auto-aprovado, Parcialmente aprovada, ou
     * qualquer status que o comprador já tenha atribuído depois disso (Em atendimento,
     * Entregue, etc.) — significa que a etapa dele já terminou. A checagem é negativa
     * (não é mais o pendente) em vez de listar cada status "final", que cresceria a cada
     * novo status adicionado ao fluxo do comprador.
     */
    private static final String PENDING_STATUS = "aguardando aprovação";

    private static final String ADMIN_ROLE = Authorities.ADMIN;

    public void validateCanInactivate(Request request, User currentUser) {
        validateRequestIsActive(request);
        validateUserIsCreator(request, currentUser);

        if (isSupervisorApprovedOrAfter(request)) {
            throw new RequestCannotBeInactivatedException();
        }
    }

    public void validateCanEdit(Request request, User currentUser) {
        validateRequestIsActive(request);

        if (isOperationalApprovedOrAfter(request)) {
            throw new RequestNotEditableException();
        }

        if (isCreator(request, currentUser)) {
            return;
        }

        validateActingRole(request, currentUser);
    }

    public void validateCrCanBeChanged(Request request, User currentUser) {
        if (normalize(request.getStatus().getName()).equals("em analise")) {
            return;
        }

        if (hasRole(currentUser, ADMIN_ROLE)) {
            return;
        }

        if (hasRole(currentUser, Authorities.COORDENADOR)) {
            validateUserIsResponsibleForCr(request, currentUser);
            return;
        }

        if (hasRole(currentUser, Authorities.SUPERVISOR) && !isMasterCr(request)) {
            validateUserIsResponsibleForCr(request, currentUser);
            return;
        }

        throw new CrNotEditableException();
    }

    private void validateRequestIsActive(Request request) {
        if (Boolean.FALSE.equals(request.getActive())) {
            throw new RequestAlreadyInactiveException();
        }
    }

    private void validateUserIsCreator(Request request, User currentUser) {
        if (!isCreator(request, currentUser)) {
            throw new AcessDeniedException();
        }
    }

    private boolean isCreator(Request request, User currentUser) {
        return request.getCreatedByUsers()
                .stream()
                .anyMatch(user -> user.getId().equals(currentUser.getId()));
    }

    private boolean isSupervisorApprovedOrAfter(Request request) {
        String statusName = normalize(request.getStatus().getName());
        return SUPERVISOR_APPROVED_OR_AFTER.contains(statusName);
    }

    private boolean isOperationalApprovedOrAfter(Request request) {
        String statusName = normalize(request.getStatus().getName());
        return OPERATIONAL_APPROVED_OR_AFTER.contains(statusName);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    public void validateCanUpdateStatus(Request request, User currentUser) {
        validateRequestIsActive(request);

        if (!isPurchasingStage(currentUser) && isFinalizedForSupervisor(request)) {
            throw new RequestNotEditableException();
        }

        validateActingRole(request, currentUser);
    }

    public void validateCanEditItems(Request request, User currentUser) {
        validateRequestIsActive(request);

        if (!isPurchasingStage(currentUser) && isFinalizedForSupervisor(request)) {
            throw new RequestNotEditableException();
        }

        if (isCreator(request, currentUser)) {
            return;
        }

        validateActingRole(request, currentUser);
    }

    /**
     * Comprador e administrador continuam podendo editar itens/status depois que o
     * supervisor finaliza a solicitação — é justamente a etapa deles que começa ali.
     */
    private boolean isPurchasingStage(User currentUser) {
        return hasRole(currentUser, Authorities.COMPRADOR) || hasRole(currentUser, ADMIN_ROLE);
    }

    private boolean isFinalizedForSupervisor(Request request) {
        String statusName = normalize(request.getStatus().getName());
        return !statusName.equals(PENDING_STATUS);
    }

    private void validateActingRole(Request request, User currentUser) {
        if (hasRole(currentUser, Authorities.COMPRADOR) || hasRole(currentUser, ADMIN_ROLE)) {
            return;
        }

        if (isMasterCr(request) && hasRole(currentUser, Authorities.SUPERVISOR)) {
            throw new AcessDeniedException();
        }

        validateUserIsResponsibleForCr(request, currentUser);
    }

    private boolean isMasterCr(Request request) {
        return request.getCrBranch() != null
                && request.getCrBranch().getCr() != null
                && Boolean.TRUE.equals(request.getCrBranch().getCr().getMaster());
    }

    private boolean hasRole(User user, String roleName) {
        return user.getRole() != null
                && normalize(user.getRole().getName()).equals(normalize(roleName));
    }

    private void validateUserIsResponsibleForCr(Request request, User currentUser) {
        if (request.getCrBranch().getResponsibleUsers() == null) {
            throw new AcessDeniedException();
        }

        boolean isResponsible = request.getCrBranch()
                .getResponsibleUsers()
                .stream()
                .anyMatch(user -> user.getId().equals(currentUser.getId()));

        if (!isResponsible) {
            throw new AcessDeniedException();
        }
    }

}
