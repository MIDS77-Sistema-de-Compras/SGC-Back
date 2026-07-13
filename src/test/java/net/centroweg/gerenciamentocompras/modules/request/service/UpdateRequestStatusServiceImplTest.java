package net.centroweg.gerenciamentocompras.modules.request.service;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestRejectionJustificationRequiredException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestStatus;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request.UpdateRequestStatusServiceImpl;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateRequestStatusServiceImplTest {

    @Mock private RequestRepository requestRepository;
    @Mock private StatusRepository statusRepository;
    @Mock private RequestMapper requestMapper;
    @Mock private CurrentUserService currentUserService;
    @Mock private RequestBusinessRuleValidator validator;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UpdateRequestStatusServiceImpl service;

    @Captor private ArgumentCaptor<Request> requestCaptor;
    @Captor private ArgumentCaptor<RequestStatusChangedEvent> eventCaptor;

    @Test
    @DisplayName("Deve salvar e publicar um único evento ao aprovar uma solicitação")
    void shouldApproveAndPublishOneEvent() {
        Scenario scenario = scenario("Em análise", "Aprovado");

        RequestResponse result = service.updateStatus(
                scenario.request().getId(),
                new UpdateRequestStatus("Aprovado", null)
        );

        assertSame(scenario.response(), result);
        assertSame(scenario.newStatus(), scenario.request().getStatus());
        verify(requestRepository).save(scenario.request());
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        assertEquals("Em análise", eventCaptor.getValue().previousStatusName());
        assertEquals("Aprovado", eventCaptor.getValue().newStatusName());
        assertNull(eventCaptor.getValue().justification());
    }

    @ParameterizedTest(name = "Deve publicar evento para o status {0}")
    @ValueSource(strings = {"Em atendimento", "Entregue", "Cancelado"})
    void shouldPublishForEveryValidStatusChange(String newStatusName) {
        Scenario scenario = scenario("Aprovado", newStatusName);

        service.updateStatus(
                scenario.request().getId(),
                new UpdateRequestStatus(newStatusName, null)
        );

        verify(requestRepository).save(scenario.request());
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals(newStatusName, eventCaptor.getValue().newStatusName());
    }

    @Test
    @DisplayName("Deve salvar justificativa com trim e incluí-la no evento de recusa")
    void shouldTrimRejectionJustificationAndPublishIt() {
        Scenario scenario = scenario("Em análise", "Recusado");

        service.updateStatus(
                scenario.request().getId(),
                new UpdateRequestStatus("Recusado", "  Orçamento indisponível.  ")
        );

        verify(requestRepository).save(requestCaptor.capture());
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals("Orçamento indisponível.", requestCaptor.getValue().getFeedback());
        assertEquals("Orçamento indisponível.", eventCaptor.getValue().justification());
    }

    @Test
    @DisplayName("Não deve salvar nem publicar evento ao recusar sem justificativa")
    void shouldRejectRefusalWithoutJustification() {
        Scenario scenario = scenarioWithoutSave("Em análise", "Recusado");

        assertThrows(
                RequestRejectionJustificationRequiredException.class,
                () -> service.updateStatus(
                        scenario.request().getId(),
                        new UpdateRequestStatus("Recusado", "   ")
                )
        );

        verify(requestRepository, never()).save(scenario.request());
        verifyNoInteractions(eventPublisher, requestMapper);
    }

    @Test
    @DisplayName("Não deve publicar evento quando o ID do status permanecer igual")
    void shouldNotPublishWhenStatusIdIsUnchanged() {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User responsible = user(20L, "Responsável", "responsavel@teste.com");
        Status currentStatus = status(5L, "Em atendimento");
        Status sameStatus = status(5L, "EM_ATENDIMENTO");
        Request request = request(100L, currentStatus, requester, responsible);
        RequestResponse response = response(100L, "Em atendimento", null);

        mockLookup(request, responsible, sameStatus);
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toDTO(request)).thenReturn(response);

        assertSame(response, service.updateStatus(
                request.getId(),
                new UpdateRequestStatus("EM_ATENDIMENTO", null)
        ));

        verify(requestRepository).save(request);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("Deve manter a autorização atual e não produzir efeitos quando negada")
    void shouldNotChangeAnythingWhenUserIsUnauthorized() {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User unauthorized = user(30L, "Não autorizado", "outro@teste.com");
        Request request = request(100L, status(1L, "Em análise"), requester, unauthorized);

        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(currentUserService.getCurrentUser()).thenReturn(unauthorized);
        doThrow(new AcessDeniedException()).when(validator).validateCanUpdateStatus(request, unauthorized);

        assertThrows(
                AcessDeniedException.class,
                () -> service.updateStatus(request.getId(), new UpdateRequestStatus("Aprovado", null))
        );

        verify(requestRepository, never()).save(request);
        verifyNoInteractions(statusRepository, eventPublisher, requestMapper);
    }

    @Test
    @DisplayName("Deve preservar feedback anterior ao mudar para um status diferente de Recusado")
    void shouldKeepPreviousFeedbackForAnotherStatus() {
        Scenario scenario = scenario("Recusado", "Em atendimento");
        scenario.request().setFeedback("Justificativa anterior");

        service.updateStatus(
                scenario.request().getId(),
                new UpdateRequestStatus("Em atendimento", null)
        );

        assertEquals("Justificativa anterior", scenario.request().getFeedback());
    }

    @Test
    void shouldThrowWhenStatusDoesNotExist() {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User responsible = user(20L, "Responsável", "responsavel@teste.com");
        Request request = request(100L, status(1L, "Em análise"), requester, responsible);

        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(currentUserService.getCurrentUser()).thenReturn(responsible);
        when(statusRepository.findByNameIgnoreCase("Inexistente")).thenReturn(Optional.empty());

        assertThrows(
                StatusNotFoundException.class,
                () -> service.updateStatus(request.getId(), new UpdateRequestStatus("Inexistente", null))
        );

        verify(requestRepository, never()).save(request);
        verifyNoInteractions(eventPublisher, requestMapper);
    }

    @Test
    void shouldThrowWhenRequestDoesNotExist() {
        when(requestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                RequestNotFoundException.class,
                () -> service.updateStatus(999L, new UpdateRequestStatus("Aprovado", null))
        );

        verifyNoInteractions(statusRepository, currentUserService, validator, eventPublisher, requestMapper);
    }

    private Scenario scenario(String previousStatusName, String newStatusName) {
        Scenario scenario = scenarioWithoutSave(previousStatusName, newStatusName);
        when(requestRepository.save(scenario.request())).thenReturn(scenario.request());
        when(requestMapper.toDTO(scenario.request())).thenReturn(scenario.response());
        return scenario;
    }

    private Scenario scenarioWithoutSave(String previousStatusName, String newStatusName) {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User responsible = user(20L, "Responsável", "responsavel@teste.com");
        Status newStatus = status(2L, newStatusName);
        Request request = request(100L, status(1L, previousStatusName), requester, responsible);
        RequestResponse response = response(100L, newStatusName, null);
        mockLookup(request, responsible, newStatus);
        return new Scenario(request, newStatus, response);
    }

    private void mockLookup(Request request, User currentUser, Status newStatus) {
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(statusRepository.findByNameIgnoreCase(newStatus.getName())).thenReturn(Optional.of(newStatus));
    }

    private Request request(Long id, Status status, User requester, User responsible) {
        CrBranch crBranch = new CrBranch(
                new Branch("Filial Centro"),
                new Cr("TI", "7940", false),
                List.of(responsible)
        );
        crBranch.setId(50L);
        Request request = new Request(crBranch, status);
        request.setId(id);
        request.setRequestDate(LocalDateTime.of(2026, 6, 26, 10, 0));
        request.setUpdatedAt(LocalDateTime.of(2026, 6, 26, 10, 5));
        request.setActive(true);
        request.getCreatedByUsers().add(requester);
        return request;
    }

    private Status status(Long id, String name) {
        Status status = new Status(name, "Status de teste");
        status.setId(id);
        return status;
    }

    private User user(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    private RequestResponse response(Long id, String statusName, String feedback) {
        LocalDateTime dateTime = LocalDateTime.of(2026, 6, 26, 10, 0);
        return new RequestResponse(
                id,
                dateTime,
                dateTime,
                50L,
                statusName,
                feedback,
                "Solicitante",
                "1234",
                List.of(),
                List.of(),
                List.of()
        );
    }

    private record Scenario(Request request, Status newStatus, RequestResponse response) {
    }
}
