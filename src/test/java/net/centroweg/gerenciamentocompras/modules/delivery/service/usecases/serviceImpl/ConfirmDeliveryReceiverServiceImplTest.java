package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryReceiverNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.service.mapper.DeliveryMapper;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.confirmRequest;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.deliveredStatus;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.delivery;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.request;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.status;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmDeliveryReceiverServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private CurrentUserService currentUserService;

    private ConfirmDeliveryReceiverServiceImpl service;
    private Status status;
    private User firstReceiver;
    private User secondReceiver;
    private User outsider;

    @BeforeEach
    void setUp() {
        service = new ConfirmDeliveryReceiverServiceImpl(
                deliveryRepository,
                statusRepository,
                currentUserService,
                new DeliveryMapper()
        );
        status = status();
        firstReceiver = user(1L, "Primeiro", true);
        secondReceiver = user(2L, "Segundo", true);
        outsider = user(3L, "Terceiro", true);

        lenient().when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldConfirmReceiver() {
        Delivery delivery = delivery(request(), status, firstReceiver, secondReceiver);
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));
        when(currentUserService.getCurrentUser()).thenReturn(firstReceiver);

        var response = service.confirmReceiver(100L, 1L, confirmRequest());

        assertThat(response.receivers())
                .filteredOn(receiver -> receiver.userId().equals(1L))
                .first()
                .extracting("confirmed")
                .isEqualTo(true);
    }

    @Test
    void shouldRejectConfirmationByDifferentAuthenticatedUser() {
        Delivery delivery = delivery(request(), status, firstReceiver, secondReceiver);
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));
        when(currentUserService.getCurrentUser()).thenReturn(firstReceiver);

        assertThatThrownBy(() -> service.confirmReceiver(100L, 2L, confirmRequest()))
                .isInstanceOf(AcessDeniedException.class);
    }

    @Test
    void shouldRejectConfirmationByUserNotAssociatedWithDelivery() {
        Delivery delivery = delivery(request(), status, firstReceiver, secondReceiver);
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));
        when(currentUserService.getCurrentUser()).thenReturn(outsider);

        assertThatThrownBy(() -> service.confirmReceiver(100L, 3L, confirmRequest()))
                .isInstanceOf(DeliveryReceiverNotFoundException.class);
    }

    @Test
    void shouldTreatRepeatedConfirmationAsIdempotent() {
        Delivery delivery = delivery(request(), status, firstReceiver, secondReceiver);
        LocalDateTime confirmedAt = LocalDateTime.now().minusHours(1);
        delivery.getReceivers().get(0).setConfirmed(true);
        delivery.getReceivers().get(0).setConfirmedAt(confirmedAt);
        delivery.getReceivers().get(0).setObservation("primeira");
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));
        when(currentUserService.getCurrentUser()).thenReturn(firstReceiver);

        service.confirmReceiver(100L, 1L, confirmRequest());

        assertThat(delivery.getReceivers().get(0).getConfirmedAt()).isEqualTo(confirmedAt);
        assertThat(delivery.getReceivers().get(0).getObservation()).isEqualTo("primeira");
    }

    @Test
    void shouldUpdateStatusWhenBothReceiversConfirmed() {
        Delivery delivery = delivery(request(), status, firstReceiver, secondReceiver);
        delivery.getReceivers().get(0).setConfirmed(true);
        delivery.getReceivers().get(0).setConfirmedAt(LocalDateTime.now().minusHours(1));
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));
        when(currentUserService.getCurrentUser()).thenReturn(secondReceiver);
        when(statusRepository.findByNameIgnoreCase("Entregue")).thenReturn(Optional.of(deliveredStatus()));

        var response = service.confirmReceiver(100L, 2L, confirmRequest());

        assertThat(response.receivers()).allMatch(receiver -> Boolean.TRUE.equals(receiver.confirmed()));
        assertThat(response.statusName()).isEqualTo("Entregue");
    }
}
