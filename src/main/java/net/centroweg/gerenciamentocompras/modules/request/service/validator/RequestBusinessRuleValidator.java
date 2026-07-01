package net.centroweg.gerenciamentocompras.modules.request.service.validator;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.*;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RequestBusinessRuleValidator {

    private static final Set<String> SUPERVISOR_APPROVED_OR_AFTER = Set.of(
            "aprovado",
            "em atendimento",
            "entregue",
            "cancelado"
    );

    private static final Set<String> OPERATIONAL_APPROVED_OR_AFTER = Set.of(
            "em atendimento",
            "entregue",
            "cancelado"
    );

    private static final String ADMIN_ROLE = "Administrador";

    public void validateCanInactivate(Request request, User currentUser) {
        validateRequestIsActive(request);
        validateUserIsCreator(request, currentUser);

        if (isSupervisorApprovedOrAfter(request)) {
            throw new RequestCannotBeInactivatedException();
        }
    }

    public void validateCanEdit(Request request, User currentUser) {
        validateRequestIsActive(request);
        validateUserIsCreator(request, currentUser);

        if (isOperationalApprovedOrAfter(request)) {
            throw new RequestNotEditableException();
        }
    }

    public void validateCrCanBeChanged(Request request, User currentUser) {
        if (normalize(request.getStatus().getName()).equals("em analise")) {
            return;
        }

        String roleName = currentUser.getRole() != null
                ? normalize(currentUser.getRole().getName())
                : "";

        if (roleName.equals(normalize(ADMIN_ROLE))) {
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
        boolean isCreator = request.getCreatedByUsers()
                .stream()
                .anyMatch(user -> user.getId().equals(currentUser.getId()));

        if (!isCreator) {
            throw new AcessDeniedException();
        }
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
        validateUserIsResponsibleForCr(request, currentUser);
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