package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryAlreadyInactiveException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.service.mapper.DeliveryMapper;
import net.centroweg.gerenciamentocompras.modules.delivery.service.validator.DeliveryItemResolver;
import net.centroweg.gerenciamentocompras.modules.delivery.service.validator.DeliveryReceiverValidator;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.service.api.StatusPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.StatusPublicData;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.delivery;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.request;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.status;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.updateRequest;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateDeliveryServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private StatusPublicApi statusPublicApi;

    @Mock
    private UserPublicApi userPublicApi;

    @Mock
    private DeliveryItemResolver deliveryItemResolver;

    private UpdateDeliveryServiceImpl service;
    private Request request;
    private Status status;
    private User firstReceiver;
    private User secondReceiver;
    private User thirdReceiver;

    @BeforeEach
    void setUp() {
        service = new UpdateDeliveryServiceImpl(
                deliveryRepository,
                statusPublicApi,
                new DeliveryReceiverValidator(userPublicApi),
                deliveryItemResolver,
                new DeliveryMapper()
        );
        request = request();
        status = status();
        firstReceiver = user(1L, "Primeiro", true);
        secondReceiver = user(2L, "Segundo", true);
        thirdReceiver = user(3L, "Terceiro", true);

        lenient().when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(deliveryItemResolver.resolveProductItems(any(Request.class), isNull())).thenReturn(List.of());
        lenient().when(deliveryItemResolver.resolveProvisionItems(any(Request.class), isNull())).thenReturn(List.of());
    }

    @Test
    void shouldUpdateDeliveryData() {
        Delivery delivery = delivery(request, status, firstReceiver, secondReceiver);
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));
        when(statusPublicApi.findById(20L)).thenReturn(Optional.of(status));
        when(userPublicApi.findUsersByIds(List.of(1L, 2L))).thenReturn(List.of(firstReceiver, secondReceiver));

        var response = service.update(100L, updateRequest(List.of(1L, 2L)));

        assertThat(response.deliveryLocation()).isEqualTo("Almoxarifado");
        assertThat(response.receivers()).hasSize(2);
    }

    @Test
    void shouldPreserveRemainingReceiverConfirmationAndAddNewReceiverUnconfirmed() {
        Delivery delivery = delivery(request, status, firstReceiver, secondReceiver);
        LocalDateTime confirmedAt = LocalDateTime.now().minusDays(1);
        delivery.getReceivers().get(0).setConfirmed(true);
        delivery.getReceivers().get(0).setConfirmedAt(confirmedAt);
        delivery.getReceivers().get(0).setObservation("preservar");

        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));
        when(statusPublicApi.findById(20L)).thenReturn(Optional.of(status));
        when(userPublicApi.findUsersByIds(List.of(1L, 3L))).thenReturn(List.of(firstReceiver, thirdReceiver));

        var response = service.update(100L, updateRequest(List.of(1L, 3L)));

        assertThat(response.receivers()).hasSize(2);
        assertThat(response.receivers())
                .filteredOn(receiver -> receiver.userId().equals(1L))
                .first()
                .satisfies(receiver -> {
                    assertThat(receiver.confirmed()).isTrue();
                    assertThat(receiver.confirmedAt()).isEqualTo(confirmedAt);
                    assertThat(receiver.observation()).isEqualTo("preservar");
                });
        assertThat(response.receivers())
                .filteredOn(receiver -> receiver.userId().equals(3L))
                .first()
                .extracting("confirmed")
                .isEqualTo(false);
    }

    @Test
    void shouldRejectUpdateOfInactiveDelivery() {
        Delivery delivery = delivery(request, status, firstReceiver, secondReceiver);
        delivery.setActive(false);
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));

        assertThatThrownBy(() -> service.update(100L, updateRequest(List.of(1L, 2L))))
                .isInstanceOf(DeliveryAlreadyInactiveException.class);
    }
}
