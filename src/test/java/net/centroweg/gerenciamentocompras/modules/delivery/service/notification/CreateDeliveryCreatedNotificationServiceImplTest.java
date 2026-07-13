package net.centroweg.gerenciamentocompras.modules.delivery.service.notification;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateDeliveryCreatedNotificationServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private NotificationRepository notificationRepository;

    private CreateDeliveryCreatedNotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CreateDeliveryCreatedNotificationServiceImpl(
                deliveryRepository,
                notificationRepository,
                new DeliveryCreatedEmailMessageFactory()
        );
    }

    @Test
    void shouldCreateInternalNotificationForEachReceiverWhenDeliveryIsCreated() {
        Delivery delivery = delivery(user(1L, "Ana", "ana@teste.com"), user(2L, "Bia", "bia@teste.com"));
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));

        service.createNotifications(new DeliveryCreatedEvent(100L, 10L, LocalDateTime.now()));

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, times(2)).save(captor.capture());

        assertThat(captor.getAllValues())
                .extracting(notification -> notification.getUser().getId())
                .containsExactly(1L, 2L);
        assertThat(captor.getAllValues().get(0).getMessage())
                .contains("Solicitacao", "Local de entrega/retirada", "Itens da entrega");
    }

    private Delivery delivery(User... users) {
        Request request = new Request();
        request.setId(10L);
        Status status = new Status("EM_ANDAMENTO", "Em andamento");

        Delivery delivery = new Delivery();
        delivery.setId(100L);
        delivery.setRequest(request);
        delivery.setStatus(status);
        delivery.setExpectedDeliveryAt(LocalDateTime.now().plusDays(1));
        delivery.setDeliveryLocation("SENAI - Almoxarifado");
        delivery.setActive(true);
        for (User user : users) {
            delivery.addReceiver(user);
        }
        return delivery;
    }

    private User user(Long id, String name, String email) {
        User user = new User(name, "52998224725", email, "Senha@123", "1234", true);
        user.setId(id);
        return user;
    }
}
