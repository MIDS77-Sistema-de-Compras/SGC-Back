package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.service.mapper.DeliveryMapper;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.service.api.StatusPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.StatusPublicData;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.delivery;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.request;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.status;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllDeliveryServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock private StatusPublicApi statusPublicApi;

    private FindAllDeliveryServiceImpl service;
    private Delivery activeDelivery;
    private Delivery inactiveDelivery;

    @BeforeEach
    void setUp() {
        service = new FindAllDeliveryServiceImpl(deliveryRepository, new DeliveryMapper(statusPublicApi));
        Request request = request();
        Status status = status();
        Mockito.lenient().when(statusPublicApi.findById(status.getId())).thenReturn(Optional.of(status));
        User firstReceiver = user(1L, "Primeiro", true);
        User secondReceiver = user(2L, "Segundo", true);
        activeDelivery = delivery(request, status, firstReceiver, secondReceiver);
        inactiveDelivery = delivery(request, status, firstReceiver, secondReceiver);
        inactiveDelivery.setId(101L);
        inactiveDelivery.setActive(false);
    }

    @Test
    void shouldListActiveDeliveriesUsingRepositoryFilter() {
        when(deliveryRepository.findByActiveTrue()).thenReturn(List.of(activeDelivery));

        var response = service.findAll(true, null, null);

        assertThat(response).hasSize(1);
        verify(deliveryRepository).findByActiveTrue();
    }

    @Test
    void shouldListByRequestId() {
        when(deliveryRepository.findByRequestId(10L)).thenReturn(List.of(activeDelivery));

        var response = service.findAll(null, 10L, null);

        assertThat(response).hasSize(1);
        verify(deliveryRepository).findByRequestId(10L);
    }

    @Test
    void shouldListByReceiverId() {
        when(deliveryRepository.findByReceiverId(1L)).thenReturn(List.of(activeDelivery));

        var response = service.findAll(null, null, 1L);

        assertThat(response).hasSize(1);
        verify(deliveryRepository).findByReceiverId(1L);
    }

    @Test
    void shouldFilterInactiveDeliveriesInMemory() {
        when(deliveryRepository.findAll()).thenReturn(List.of(activeDelivery, inactiveDelivery));

        var response = service.findAll(false, null, null);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).active()).isFalse();
    }
}
