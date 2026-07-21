package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryAccessDeniedException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryReceiverNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryStatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.ConfirmDeliveryReceiverRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.service.mapper.DeliveryMapper;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.confirmRequest;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.deliveredStatus;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.delivery;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.request;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.status;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.user;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.service.api.StatusPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;

@ExtendWith(MockitoExtension.class)
class ConfirmDeliveryReceiverServiceImplTest {

    @Mock DeliveryRepository deliveryRepository;
    @Mock StatusPublicApi statusPublicApi;
    @Mock CurrentUserService currentUserService;
    @Mock CompleteRequestOnDeliveryStatusServiceImpl completeRequestOnDeliveryStatusService;

    private ConfirmDeliveryReceiverServiceImpl service;
    private Status pending;
    private User first;
    private User second;

    @BeforeEach
    void setUp() {
        service = new ConfirmDeliveryReceiverServiceImpl(
                deliveryRepository, statusPublicApi, currentUserService, new DeliveryMapper(),
                completeRequestOnDeliveryStatusService);
        pending = status();
        first = user(1L, "Primeiro", true);
        second = user(2L, "Segundo", true);
        lenient().when(statusPublicApi.findById(20L)).thenReturn(Optional.of(pending));
        lenient().when(statusPublicApi.findById(30L)).thenReturn(Optional.of(deliveredStatus()));
        lenient().when(deliveryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldConfirmFirstReceiverWithoutCompletingDelivery() {
        Delivery delivery = delivery(request(), pending, first, second);
        mockDelivery(delivery, first);

        var response = service.confirmReceiver(100L, 1L, new ConfirmDeliveryReceiverRequest("  conferido  "));

        assertThat(response.receivers().getFirst().confirmed()).isTrue();
        assertThat(response.receivers().getFirst().confirmedAt()).isNotNull();
        assertThat(response.receivers().getFirst().observation()).isEqualTo("conferido");
        assertThat(delivery.getDeliveredAt()).isNull();
        verify(completeRequestOnDeliveryStatusService).apply(delivery);
    }

    @Test
    void shouldCompleteDeliveryWithOneConsistentTimestamp() {
        Delivery delivery = delivery(request(), pending, first, second);
        delivery.getReceivers().getFirst().setConfirmed(true);
        delivery.getReceivers().getFirst().setConfirmedAt(LocalDateTime.now().minusHours(1));
        mockDelivery(delivery, second);
        when(statusPublicApi.findByName("Entregue")).thenReturn(Optional.of(deliveredStatus()));

        var response = service.confirmReceiver(100L, 2L, confirmRequest());

        assertThat(response.statusName()).isEqualTo("Entregue");
        assertThat(response.deliveredAt()).isEqualTo(response.receivers().get(1).confirmedAt());
        assertThat(response.receivers()).allMatch(value -> Boolean.TRUE.equals(value.confirmed()));
        verify(completeRequestOnDeliveryStatusService).apply(delivery);
    }

    @Test
    void shouldKeepConfirmedAtDeliveredAtAndObservationOnRepeatedConfirmation() {
        Delivery delivery = delivery(request(), deliveredStatus(), first, second);
        LocalDateTime original = LocalDateTime.now().minusHours(2);
        delivery.setDeliveredAt(original);
        delivery.getReceivers().getFirst().setConfirmed(true);
        delivery.getReceivers().getFirst().setConfirmedAt(original);
        delivery.getReceivers().getFirst().setObservation("original");
        mockDelivery(delivery, first);

        service.confirmReceiver(100L, 1L, new ConfirmDeliveryReceiverRequest("alterar"));

        assertThat(delivery.getDeliveredAt()).isEqualTo(original);
        assertThat(delivery.getReceivers().getFirst().getConfirmedAt()).isEqualTo(original);
        assertThat(delivery.getReceivers().getFirst().getObservation()).isEqualTo("original");
        verify(deliveryRepository, never()).save(any());
        verifyNoInteractions(completeRequestOnDeliveryStatusService);
    }

    @Test
    void shouldFailWithoutDeliveredStatusAndLeaveReceiverUnchanged() {
        Delivery delivery = delivery(request(), pending, first, second);
        delivery.getReceivers().getFirst().setConfirmed(true);
        mockDelivery(delivery, second);
        when(statusPublicApi.findByName("Entregue")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.confirmReceiver(100L, 2L, confirmRequest()))
                .isInstanceOf(DeliveryStatusNotFoundException.class);
        assertThat(delivery.getReceivers().get(1).getConfirmed()).isFalse();
        assertThat(delivery.getDeliveredAt()).isNull();
        verifyNoInteractions(completeRequestOnDeliveryStatusService);
    }

    @Test
    void shouldRejectDifferentAuthenticatedUser() {
        Delivery delivery = delivery(request(), pending, first, second);
        mockDelivery(delivery, first);
        assertThatThrownBy(() -> service.confirmReceiver(100L, 2L, confirmRequest()))
                .isInstanceOf(DeliveryAccessDeniedException.class);
    }

    @Test
    void shouldRejectUserNotAssociated() {
        User outsider = user(3L, "Terceiro", true);
        Delivery delivery = delivery(request(), pending, first, second);
        mockDelivery(delivery, outsider);
        assertThatThrownBy(() -> service.confirmReceiver(100L, 3L, confirmRequest()))
                .isInstanceOf(DeliveryReceiverNotFoundException.class);
    }

    private void mockDelivery(Delivery delivery, User authenticated) {
        when(deliveryRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delivery));
        when(currentUserService.getCurrentUser()).thenReturn(authenticated);
    }
}
