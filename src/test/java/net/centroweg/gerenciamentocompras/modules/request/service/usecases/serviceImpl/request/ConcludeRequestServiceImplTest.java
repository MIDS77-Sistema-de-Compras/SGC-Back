package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcludeRequestServiceImplTest {

    @Mock private RequestRepository requestRepository;
    @Mock private StatusRepository statusRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private CurrentUserService currentUserService;

    @InjectMocks
    private ConcludeRequestServiceImpl service;

    @Captor private ArgumentCaptor<RequestStatusChangedEvent> eventCaptor;

    @Test
    @DisplayName("Deve concluir a solicitação e publicar o evento quando o status muda")
    void shouldConcludeRequestAndPublishEventWhenStatusChanges() {
        User agent = user(1L, "Sistema", "sistema@teste.com");
        Status previousStatus = status(1L, "Em atendimento");
        Status concludedStatus = status(2L, "ENTREGUE");
        Request request = request(100L, previousStatus);

        when(requestRepository.findById(100L)).thenReturn(Optional.of(request));
        when(statusRepository.findByNameIgnoreCase("ENTREGUE")).thenReturn(Optional.of(concludedStatus));
        when(requestRepository.save(request)).thenReturn(request);
        when(currentUserService.getCurrentUser()).thenReturn(agent);

        service.concludeRequest(100L, "ENTREGUE");

        assertSame(concludedStatus, request.getStatus());
        verify(requestRepository).save(request);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals(100L, eventCaptor.getValue().requestId());
        assertEquals(1L, eventCaptor.getValue().previousStatusId());
        assertEquals("Em atendimento", eventCaptor.getValue().previousStatusName());
        assertEquals(2L, eventCaptor.getValue().newStatusId());
        assertEquals("ENTREGUE", eventCaptor.getValue().newStatusName());
        assertNull(eventCaptor.getValue().justification());
        assertEquals(1L, eventCaptor.getValue().changedByUserId());
        assertEquals("Sistema", eventCaptor.getValue().changedByUserName());
    }

    @Test
    @DisplayName("Não deve fazer nada quando a solicitação já está concluída")
    void shouldDoNothingWhenRequestAlreadyConcluded() {
        Status concludedStatus = status(2L, "PEDIDO CANCELADO");
        Request request = request(100L, status(2L, "PEDIDO CANCELADO"));

        when(requestRepository.findById(100L)).thenReturn(Optional.of(request));
        when(statusRepository.findByNameIgnoreCase("PEDIDO CANCELADO")).thenReturn(Optional.of(concludedStatus));

        service.concludeRequest(100L, "PEDIDO CANCELADO");

        verify(requestRepository, never()).save(request);
        verifyNoInteractions(eventPublisher, currentUserService);
    }

    @Test
    @DisplayName("Deve lançar StatusNotFoundException quando o status Concluída não existir")
    void shouldThrowStatusNotFoundExceptionWhenConcludedStatusMissing() {
        Request request = request(100L, status(1L, "Em atendimento"));

        when(requestRepository.findById(100L)).thenReturn(Optional.of(request));
        when(statusRepository.findByNameIgnoreCase("ENTREGUE")).thenReturn(Optional.empty());

        assertThrows(StatusNotFoundException.class, () -> service.concludeRequest(100L, "ENTREGUE"));

        verify(requestRepository, never()).save(request);
        verifyNoInteractions(eventPublisher, currentUserService);
    }

    @Test
    @DisplayName("Deve lançar RequestNotFoundException quando a solicitação não existir")
    void shouldThrowRequestNotFoundExceptionWhenRequestMissing() {
        when(requestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () -> service.concludeRequest(999L, "ENTREGUE"));

        verifyNoInteractions(statusRepository, eventPublisher, currentUserService);
    }

    private Request request(Long id, Status status) {
        CrBranch crBranch = new CrBranch(new Branch("Filial Centro"), new Cr("TI", "7940", false), null);
        crBranch.setId(50L);
        Request request = new Request(crBranch, status);
        request.setId(id);
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
}
