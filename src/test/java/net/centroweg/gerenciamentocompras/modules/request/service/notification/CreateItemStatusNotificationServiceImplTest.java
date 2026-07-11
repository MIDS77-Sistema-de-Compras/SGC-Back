package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateItemStatusNotificationServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private NotificationRepository notificationRepository;

    private CreateItemStatusNotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CreateItemStatusNotificationServiceImpl(
                requestRepository,
                deliveryRepository,
                notificationRepository,
                new ItemStatusEmailMessageFactory()
        );
    }

    @Test
    void shouldCreateInternalNotificationForEachRequesterWhenItemStatusChanges() {
        Request request = requestWithRequesters(user(1L, "Ana", "ana@teste.com"), user(2L, "Bia", "bia@teste.com"));
        ItemStatusChangedEvent event = statusChangedEvent();

        when(requestRepository.findWithRequestersById(10L)).thenReturn(Optional.of(request));
        when(deliveryRepository.findByRequestIdAndProductItemId(10L, 99L)).thenReturn(List.of());

        service.createNotifications(event);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, org.mockito.Mockito.times(2)).save(captor.capture());

        assertThat(captor.getAllValues())
                .extracting(notification -> notification.getUser().getId())
                .containsExactly(1L, 2L);
        assertThat(captor.getAllValues().get(0).getMessage())
                .contains("Solicitacao atualizada", "Status anterior", "Novo status");
    }

    private ItemStatusChangedEvent statusChangedEvent() {
        return new ItemStatusChangedEvent(
                10L,
                99L,
                RequestItemType.PRODUCT,
                "Parafuso",
                "PRD-1",
                2.0,
                "UN",
                "EM_ANDAMENTO",
                "Entregue",
                "Sem observacao",
                LocalDateTime.now()
        );
    }

    private Request requestWithRequesters(User... users) {
        Request request = new Request();
        request.setId(10L);
        request.getCreatedByUsers().addAll(List.of(users));
        return request;
    }

    private User user(Long id, String name, String email) {
        User user = new User(name, "52998224725", email, "Senha@123", "1234", true);
        user.setId(id);
        return user;
    }
}
