package net.centroweg.gerenciamentocompras.modules.request.service.validator;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestAlreadyInactiveException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestCannotBeInactivatedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotEditableException;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestBusinessRuleValidatorTest {

    private final RequestBusinessRuleValidator validator = new RequestBusinessRuleValidator();

    @Test
    @DisplayName("Deve permitir editar solicitação ativa pelo próprio criador antes da etapa operacional")
    void shouldAllowEditWhenRequestIsActiveCreatorAndStatusAllowsIt() {
        User creator = user(1L);
        Request request = request("Pendente", true, creator);

        assertDoesNotThrow(() -> validator.validateCanEdit(request, creator));
    }
    @Test
    @DisplayName("Deve bloquear edição quando a solicitação estiver em atendimento")
    void shouldBlockEditWhenRequestIsInService() {
        User creator = user(1L);
        Request request = request("Em atendimento", true, creator);

        assertThrows(RequestNotEditableException.class, () -> validator.validateCanEdit(request, creator));
    }

    @Test
    @DisplayName("Deve bloquear edição quando o usuário não for o criador")
    void shouldBlockEditWhenCurrentUserIsNotCreator() {
        Request request = request("Pendente", true, user(1L));

        assertThrows(AcessDeniedException.class, () -> validator.validateCanEdit(request, user(2L)));
    }

    @Test
    @DisplayName("Deve bloquear edição quando a solicitação estiver inativa")
    void shouldBlockEditWhenRequestIsInactive() {
        User creator = user(1L);
        Request request = request("Pendente", false, creator);

        assertThrows(RequestAlreadyInactiveException.class, () -> validator.validateCanEdit(request, creator));
    }

    @Test
    @DisplayName("Deve permitir inativar solicitação ativa pelo próprio criador antes da aprovação")
    void shouldAllowInactivationWhenRequestIsActiveCreatorAndNotApproved() {
        User creator = user(1L);
        Request request = request("Pendente", true, creator);

        assertDoesNotThrow(() -> validator.validateCanInactivate(request, creator));
    }

    @Test
    @DisplayName("Deve bloquear inativação quando a solicitação estiver aprovada")
    void shouldBlockInactivationWhenRequestIsApproved() {
        User creator = user(1L);
        Request request = request("Aprovado", true, creator);

        assertThrows(RequestCannotBeInactivatedException.class, () -> validator.validateCanInactivate(request, creator));
    }
    @Test
    @DisplayName("Deve bloquear inativação quando a solicitação estiver em atendimento")
    void shouldBlockInactivationWhenRequestIsInService() {
        User creator = user(1L);
        Request request = request("Em atendimento", true, creator);

        assertThrows(RequestCannotBeInactivatedException.class, () -> validator.validateCanInactivate(request, creator));
    }

    @Test
    @DisplayName("Deve bloquear inativação quando o usuário não for o criador")
    void shouldBlockInactivationWhenCurrentUserIsNotCreator() {
        Request request = request("Pendente", true, user(1L));

        assertThrows(AcessDeniedException.class, () -> validator.validateCanInactivate(request, user(2L)));
    }

    @Test
    @DisplayName("Deve bloquear nova inativação quando a solicitação já estiver inativa")
    void shouldBlockInactivationWhenRequestIsInactive() {
        User creator = user(1L);
        Request request = request("Pendente", false, creator);

        assertThrows(RequestAlreadyInactiveException.class, () -> validator.validateCanInactivate(request, creator));
    }

    private Request request(String statusName, boolean active, User creator) {
        Request request = new Request();
        request.setStatus(new Status(statusName, "Status de teste"));
        request.setActive(active);
        request.getCreatedByUsers().add(creator);
        return request;
    }

    private User user(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}