package net.centroweg.gerenciamentocompras.modules.request.service.validator;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.*;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Component;
import java.util.Set;

/**
 * Componente responsável pela validação das regras de negócio de uma {@link Request}.
 */
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

    /**
     * Valida se uma solicitação pode ser inativada.
     * @param request dados da solicitação.
     * @param currentUser dados do usuário autenticado.
     * @throws RequestAlreadyInactiveException caso a solicitação já esteja inativa.
     * @throws AcessDeniedException caso o usuário não seja o criador da solicitação.
     * @throws RequestCannotBeInactivatedException caso a solicitação já esteja aprovada ou em etapa posterior.
     */
    public void validateCanInactivate(Request request, User currentUser) {
        validateRequestIsActive(request);
        validateUserIsCreator(request, currentUser);

        if (isSupervisorApprovedOrAfter(request)) {
            throw new RequestCannotBeInactivatedException();
        }
    }

    /**
     * Valida se uma solicitação pode ser editada.
     * @param request dados da solicitação.
     * @param currentUser dados do usuário autenticado.
     * @throws RequestAlreadyInactiveException caso a solicitação já esteja inativa.
     * @throws AcessDeniedException caso o usuário não seja o criador da solicitação.
     * @throws RequestNotEditableException caso a solicitação já esteja em atendimento ou em etapa posterior.
     */
    public void validateCanEdit(Request request, User currentUser) {
        validateRequestIsActive(request);
        validateUserIsCreator(request, currentUser);

        if (isOperationalApprovedOrAfter(request)) {
            throw new RequestNotEditableException();
        }
    }

    /**
     * Valida se a filial/CR de uma solicitação pode ser alterada.
     * @param request dados da solicitação.
     * @param currentUser dados do usuário autenticado.
     * @throws CrNotEditableException caso a filial/CR não possa ser alterada.
     */
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

    /**
     * Valida se uma solicitação está ativa.
     * @param request dados da solicitação.
     * @throws RequestAlreadyInactiveException caso a solicitação já esteja inativa.
     */
    private void validateRequestIsActive(Request request) {
        if (Boolean.FALSE.equals(request.getActive())) {
            throw new RequestAlreadyInactiveException();
        }
    }

    /**
     * Valida se o usuário informado é o criador de uma solicitação.
     * @param request dados da solicitação.
     * @param currentUser dados do usuário autenticado.
     * @throws AcessDeniedException caso o usuário não seja o criador da solicitação.
     */
    private void validateUserIsCreator(Request request, User currentUser) {
        boolean isCreator = request.getCreatedByUsers()
                .stream()
                .anyMatch(user -> user.getId().equals(currentUser.getId()));

        if (!isCreator) {
            throw new AcessDeniedException();
        }
    }

    /**
     * Verifica se uma solicitação está aprovada ou em etapa posterior sob a ótica do supervisor.
     * @param request dados da solicitação.
     * @return {@code true} caso a solicitação esteja aprovada ou em etapa posterior, {@code false} caso contrário.
     */
    private boolean isSupervisorApprovedOrAfter(Request request) {
        String statusName = normalize(request.getStatus().getName());
        return SUPERVISOR_APPROVED_OR_AFTER.contains(statusName);
    }

    /**
     * Verifica se uma solicitação está em atendimento ou em etapa posterior sob a ótica operacional.
     * @param request dados da solicitação.
     * @return {@code true} caso a solicitação esteja em atendimento ou em etapa posterior, {@code false} caso contrário.
     */
    private boolean isOperationalApprovedOrAfter(Request request) {
        String statusName = normalize(request.getStatus().getName());
        return OPERATIONAL_APPROVED_OR_AFTER.contains(statusName);
    }

    /**
     * Normaliza um valor textual removendo espaços nas extremidades e convertendo para minúsculas.
     * @param value valor a ser normalizado.
     * @return valor normalizado, ou vazio caso o valor seja nulo.
     */
    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    /**
     * Valida se o status de uma solicitação pode ser atualizado.
     * @param request dados da solicitação.
     * @param currentUser dados do usuário autenticado.
     * @throws RequestAlreadyInactiveException caso a solicitação já esteja inativa.
     * @throws AcessDeniedException caso o usuário não seja responsável pela filial/CR da solicitação.
     */
    public void validateCanUpdateStatus(Request request, User currentUser) {
        validateRequestIsActive(request);
        validateUserIsResponsibleForCr(request, currentUser);
    }

    /**
     * Valida se o usuário informado é responsável pela filial/CR de uma solicitação.
     * @param request dados da solicitação.
     * @param currentUser dados do usuário autenticado.
     * @throws AcessDeniedException caso o usuário não seja responsável pela filial/CR da solicitação.
     */
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