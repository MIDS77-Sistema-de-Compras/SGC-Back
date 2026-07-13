package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import net.centroweg.gerenciamentocompras.modules.notification.service.api.NotificationPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRequestStatusNotificationServiceImplTest {

    @Mock private RequestRepository requestRepository;
    @Mock private NotificationPublicApi notificationPublicApi;

    private CreateRequestStatusNotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CreateRequestStatusNotificationServiceImpl(
                requestRepository,
                notificationPublicApi,
                new RequestStatusEmailMessageFactory("http://localhost/docente/solicitacoes/{requestId}")
        );
    }

    @Test
    void shouldCreateOneInternalNotificationForEachDistinctRequesterId() {
        User ana = user(1L, "Ana", "ana@teste.com");
        User bia = user(2L, "Bia", "bia@teste.com");
        Request request = request(10L, ana, bia, ana);
        RequestStatusChangedEvent event = event("Aprovado", "Em atendimento", null);
        when(requestRepository.findWithRequestersById(10L)).thenReturn(Optional.of(request));

        service.createNotifications(event);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Collection<Long>> idsCaptor = ArgumentCaptor.forClass(Collection.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(notificationPublicApi).createInternalNotifications(
                org.mockito.ArgumentMatchers.eq("Status da solicitação atualizado"),
                messageCaptor.capture(),
                org.mockito.ArgumentMatchers.eq(10L),
                idsCaptor.capture()
        );

        assertThat(idsCaptor.getValue()).containsExactly(1L, 2L);
        assertThat(messageCaptor.getValue())
                .contains("#10", "Aprovado", "Em atendimento")
                .doesNotContain("Justificativa");
    }

    @Test
    void shouldIncludeTrimmedJustificationInRefusalNotification() {
        Request request = request(10L, user(1L, "Ana", "ana@teste.com"));
        RequestStatusChangedEvent event = event("Em análise", "Recusado", "Orçamento indisponível");
        when(requestRepository.findWithRequestersById(10L)).thenReturn(Optional.of(request));

        service.createNotifications(event);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(notificationPublicApi).createInternalNotifications(
                org.mockito.ArgumentMatchers.anyString(),
                messageCaptor.capture(),
                org.mockito.ArgumentMatchers.eq(10L),
                org.mockito.ArgumentMatchers.anyCollection()
        );
        assertThat(messageCaptor.getValue()).contains("Recusado", "Justificativa: Orçamento indisponível");
    }

    private RequestStatusChangedEvent event(String previousStatus, String newStatus, String justification) {
        return new RequestStatusChangedEvent(
                10L,
                previousStatus,
                newStatus,
                justification,
                50L,
                "Responsável",
                LocalDateTime.of(2026, 7, 13, 15, 30)
        );
    }

    private Request request(Long id, User... users) {
        Request request = new Request();
        request.setId(id);
        request.getCreatedByUsers().addAll(List.of(users));
        return request;
    }

    private User user(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
