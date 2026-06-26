package net.centroweg.gerenciamentocompras.modules.request.service;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestRejectionJustificationRequiredException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestStatus;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request.UpdateRequestStatusServiceImpl;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRequestStatusServiceImplTest {

    @Mock private RequestRepository requestRepository;
    @Mock private StatusRepository statusRepository;
    @Mock private RequestMapper requestMapper;
    @Mock private CurrentUserService currentUserService;
    @Mock private RequestBusinessRuleValidator validator;
    @Mock private NotificationService notificationService;

    @InjectMocks
    private UpdateRequestStatusServiceImpl service;

    @Captor private ArgumentCaptor<Request> requestCaptor;
    @Captor private ArgumentCaptor<NotificationRequest> notificationCaptor;

    @Test
    @DisplayName("Deve aprovar uma solicitacao com sucesso")
    void shouldApproveRequestSuccessfully() {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User responsible = user(20L, "Responsavel", "responsavel@teste.com");
        Status approved = status(2L, "Aprovado");
        Request request = request(100L, status(1L, "Pendente"), requester, responsible);
        RequestResponse expected = response(100L, "Aprovado", null);

        mockLookup(request, responsible, approved);
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toDTO(request)).thenReturn(expected);

        RequestResponse response = service.updateStatus(request.getId(), new UpdateRequestStatus("Aprovado", null));

        assertSame(expected, response);
        assertSame(approved, request.getStatus());
        assertNull(request.getFeedback());
        verify(validator).validateCanUpdateStatus(request, responsible);
        verify(requestRepository).save(request);
    }

    @Test
    @DisplayName("Deve recusar uma solicitacao com justificativa")
    void shouldRefuseRequestWithJustification() {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User responsible = user(20L, "Responsavel", "responsavel@teste.com");
        Status refused = status(3L, "Recusado");
        Request request = request(100L, status(1L, "Pendente"), requester, responsible);
        RequestResponse expected = response(100L, "Recusado", "Faltam informacoes");

        mockLookup(request, responsible, refused);
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toDTO(request)).thenReturn(expected);

        RequestResponse response = service.updateStatus(
                request.getId(),
                new UpdateRequestStatus("Recusado", "  Faltam informacoes  ")
        );

        assertSame(expected, response);
        assertSame(refused, request.getStatus());
        assertEquals("Faltam informacoes", request.getFeedback());
    }

    @Test
    @DisplayName("Deve lancar excecao ao recusar sem justificativa")
    void shouldThrowWhenRefusingWithoutJustification() {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User responsible = user(20L, "Responsavel", "responsavel@teste.com");
        Status refused = status(3L, "Recusado");
        Request request = request(100L, status(1L, "Pendente"), requester, responsible);

        mockLookup(request, responsible, refused);

        assertThrows(
                RequestRejectionJustificationRequiredException.class,
                () -> service.updateStatus(request.getId(), new UpdateRequestStatus("Recusado", " "))
        );

        verify(requestRepository, never()).save(request);
        verifyNoInteractions(notificationService, requestMapper);
    }

    @Test
    @DisplayName("Deve lancar StatusNotFoundException quando status nao existir")
    void shouldThrowWhenStatusDoesNotExist() {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User responsible = user(20L, "Responsavel", "responsavel@teste.com");
        Request request = request(100L, status(1L, "Pendente"), requester, responsible);

        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(currentUserService.getCurrentUser()).thenReturn(responsible);
        when(statusRepository.findByNameIgnoreCase("Inexistente")).thenReturn(Optional.empty());

        assertThrows(
                StatusNotFoundException.class,
                () -> service.updateStatus(request.getId(), new UpdateRequestStatus("Inexistente", null))
        );

        verify(validator).validateCanUpdateStatus(request, responsible);
        verify(requestRepository, never()).save(request);
        verifyNoInteractions(notificationService, requestMapper);
    }

    @Test
    @DisplayName("Deve lancar RequestNotFoundException quando solicitacao nao existir")
    void shouldThrowWhenRequestDoesNotExist() {
        when(requestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                RequestNotFoundException.class,
                () -> service.updateStatus(999L, new UpdateRequestStatus("Aprovado", null))
        );

        verifyNoInteractions(statusRepository, currentUserService, validator, notificationService, requestMapper);
    }

    @Test
    @DisplayName("Deve notificar o solicitante quando a solicitacao for aprovada")
    void shouldNotifyRequesterWhenApproved() {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User responsible = user(20L, "Responsavel", "responsavel@teste.com");
        Status approved = status(2L, "Aprovado");
        Request request = request(100L, status(1L, "Pendente"), requester, responsible);

        mockLookup(request, responsible, approved);
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toDTO(request)).thenReturn(response(100L, "Aprovado", null));

        service.updateStatus(request.getId(), new UpdateRequestStatus("Aprovado", null));

        verify(notificationService).createNotification(notificationCaptor.capture());
        NotificationRequest notification = notificationCaptor.getValue();
        assertEquals("Solicitacao aprovada", normalize(notification.title()));
        assertEquals(requester.getId(), notification.userId());
        assertEquals(request.getId(), notification.requestId());
    }

    @Test
    @DisplayName("Deve notificar o solicitante quando a solicitacao for recusada")
    void shouldNotifyRequesterWhenRefused() {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User responsible = user(20L, "Responsavel", "responsavel@teste.com");
        Status refused = status(3L, "Recusado");
        Request request = request(100L, status(1L, "Pendente"), requester, responsible);

        mockLookup(request, responsible, refused);
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toDTO(request)).thenReturn(response(100L, "Recusado", "Sem verba"));

        service.updateStatus(request.getId(), new UpdateRequestStatus("Recusado", "Sem verba"));

        verify(notificationService).createNotification(notificationCaptor.capture());
        NotificationRequest notification = notificationCaptor.getValue();
        assertEquals("Solicitacao recusada", normalize(notification.title()));
        assertTrue(normalize(notification.message()).contains("Sem verba"));
        assertEquals(requester.getId(), notification.userId());
        assertEquals(request.getId(), notification.requestId());
    }

    @Test
    @DisplayName("Deve salvar feedback somente quando for recusa")
    void shouldSaveFeedbackOnlyWhenRefused() {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User responsible = user(20L, "Responsavel", "responsavel@teste.com");
        Status approved = status(2L, "Aprovado");
        Request request = request(100L, status(1L, "Pendente"), requester, responsible);

        mockLookup(request, responsible, approved);
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toDTO(request)).thenReturn(response(100L, "Aprovado", null));

        service.updateStatus(request.getId(), new UpdateRequestStatus("Aprovado", "Nao deve salvar"));

        verify(requestRepository).save(requestCaptor.capture());
        assertNull(requestCaptor.getValue().getFeedback());
    }

    @Test
    @DisplayName("Nao deve alterar outros campos alem de status e feedback")
    void shouldNotChangeOtherFields() {
        User requester = user(10L, "Solicitante", "solicitante@teste.com");
        User responsible = user(20L, "Responsavel", "responsavel@teste.com");
        Status refused = status(3L, "Recusado");
        Request request = request(100L, status(1L, "Pendente"), requester, responsible);
        CrBranch originalCrBranch = request.getCrBranch();
        LocalDateTime originalRequestDate = request.getRequestDate();
        LocalDateTime originalUpdatedAt = request.getUpdatedAt();
        Boolean originalActive = request.getActive();
        List<User> originalRequesters = request.getCreatedByUsers();

        mockLookup(request, responsible, refused);
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toDTO(request)).thenReturn(response(100L, "Recusado", "Sem verba"));

        service.updateStatus(request.getId(), new UpdateRequestStatus("Recusado", "Sem verba"));

        verify(requestRepository).save(requestCaptor.capture());
        Request saved = requestCaptor.getValue();
        assertSame(originalCrBranch, saved.getCrBranch());
        assertSame(originalRequestDate, saved.getRequestDate());
        assertSame(originalUpdatedAt, saved.getUpdatedAt());
        assertEquals(originalActive, saved.getActive());
        assertSame(originalRequesters, saved.getCreatedByUsers());
    }

    private void mockLookup(Request request, User currentUser, Status newStatus) {
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(statusRepository.findByNameIgnoreCase(newStatus.getName())).thenReturn(Optional.of(newStatus));
    }

    private Request request(Long id, Status status, User requester, User responsible) {
        CrBranch crBranch = new CrBranch(new Branch("Filial Centro"), new Cr("TI", "7940", false), List.of(responsible));
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
        return new RequestResponse(id, dateTime, dateTime, 50L, statusName, feedback, "Solicitante", "1234", List.of());
    }

    private String normalize(String value) {
        return value.replace("ç", "c").replace("ã", "a").replace("á", "a").replace("é", "e");
    }
}
