package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.InvalidDeliveryReceiversException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;
import net.centroweg.gerenciamentocompras.modules.delivery.service.mapper.DeliveryMapper;
import net.centroweg.gerenciamentocompras.modules.delivery.service.validator.DeliveryItemResolver;
import net.centroweg.gerenciamentocompras.modules.delivery.service.validator.DeliveryReceiverValidator;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryStatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.StatusPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.StatusPublicData;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.createRequest;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.request;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.status;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateDeliveryServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private RequestPublicApi requestPublicApi;

    @Mock
    private StatusPublicApi statusPublicApi;

    @Mock
    private UserPublicApi userPublicApi;

    @Mock
    private DeliveryItemResolver deliveryItemResolver;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private CreateDeliveryServiceImpl service;
    private Request request;
    private Status status;
    private User firstReceiver;
    private User secondReceiver;

    @BeforeEach
    void setUp() {
        service = new CreateDeliveryServiceImpl(
                deliveryRepository,
                requestPublicApi,
                statusPublicApi,
                new DeliveryReceiverValidator(userPublicApi),
                deliveryItemResolver,
                new DeliveryMapper(statusPublicApi),
                eventPublisher
        );
        request = request();
        status = status();
        firstReceiver = user(1L, "Primeiro", true);
        secondReceiver = user(2L, "Segundo", true);

        lenient().when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> {
            Delivery delivery = invocation.getArgument(0);
            delivery.setId(100L);
            return delivery;
        });
        lenient().when(deliveryItemResolver.resolveProductItems(any(Request.class), isNull())).thenReturn(List.of());
        lenient().when(deliveryItemResolver.resolveProvisionItems(any(Request.class), isNull())).thenReturn(List.of());
    }

    @Test
    void shouldCreateDeliveryWithTwoValidReceivers() {
        mockRequestStatusAndReceivers(List.of(firstReceiver, secondReceiver));

        var response = service.create(createRequest(List.of(1L, 2L)));

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.receivers()).hasSize(2);

        ArgumentCaptor<DeliveryCreatedEvent> eventCaptor = ArgumentCaptor.forClass(DeliveryCreatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().deliveryId()).isEqualTo(100L);
        assertThat(eventCaptor.getValue().requestId()).isEqualTo(10L);
    }

    @Test
    void shouldRejectInvalidReceiverCount() {
        mockRequestAndStatus();

        assertThatThrownBy(() -> service.create(createRequest(List.of(1L))))
                .isInstanceOf(InvalidDeliveryReceiversException.class);
    }

    @Test
    void shouldRejectDuplicateReceiverIds() {
        mockRequestAndStatus();

        assertThatThrownBy(() -> service.create(createRequest(List.of(1L, 1L))))
                .isInstanceOf(InvalidDeliveryReceiversException.class);
    }

    @Test
    void shouldRejectUnknownReceiver() {
        mockRequestAndStatus();
        when(userPublicApi.findUsersByIds(List.of(1L, 2L))).thenReturn(List.of(firstReceiver));

        assertThatThrownBy(() -> service.create(createRequest(List.of(1L, 2L))))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldRejectInactiveReceiver() {
        mockRequestAndStatus();
        User inactive = user(2L, "Inativo", false);
        when(userPublicApi.findUsersByIds(List.of(1L, 2L))).thenReturn(List.of(firstReceiver, inactive));

        assertThatThrownBy(() -> service.create(createRequest(List.of(1L, 2L))))
                .isInstanceOf(InvalidDeliveryReceiversException.class);
    }

    @Test
    void shouldRejectUnknownRequest() {
        when(requestPublicApi.findRequestById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(createRequest(List.of(1L, 2L))))
                .isInstanceOf(RequestNotFoundException.class);
    }

    @Test
    void shouldRejectUnknownStatus() {
        when(requestPublicApi.findRequestById(10L)).thenReturn(Optional.of(request));
        when(statusPublicApi.findById(20L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(createRequest(List.of(1L, 2L))))
                .isInstanceOf(DeliveryStatusNotFoundException.class);
    }

    private void mockRequestAndStatus() {
        when(requestPublicApi.findRequestById(10L)).thenReturn(Optional.of(request));
        when(statusPublicApi.findById(20L)).thenReturn(Optional.of(status));
    }

    private void mockRequestStatusAndReceivers(List<User> receivers) {
        mockRequestAndStatus();
        when(userPublicApi.findUsersByIds(List.of(1L, 2L))).thenReturn(receivers);
    }
}
