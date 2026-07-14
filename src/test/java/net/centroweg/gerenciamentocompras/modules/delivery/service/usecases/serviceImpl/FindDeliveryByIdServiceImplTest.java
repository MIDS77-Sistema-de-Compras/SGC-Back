package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryNotFoundException;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.delivery;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.request;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.status;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindDeliveryByIdServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock private StatusPublicApi statusPublicApi;

    private FindDeliveryByIdServiceImpl service;
    private Delivery delivery;

    @BeforeEach
    void setUp() {
        service = new FindDeliveryByIdServiceImpl(deliveryRepository, new DeliveryMapper(statusPublicApi));
        Request request = request();
        Status status = status();
        org.mockito.Mockito.lenient().when(statusPublicApi.findById(status.getId())).thenReturn(Optional.of(status));
        User firstReceiver = user(1L, "Primeiro", true);
        User secondReceiver = user(2L, "Segundo", true);
        delivery = delivery(request, status, firstReceiver, secondReceiver);
    }

    @Test
    void shouldFindExistingDelivery() {
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));

        var response = service.findById(100L);

        assertThat(response.id()).isEqualTo(100L);
    }

    @Test
    void shouldFailWhenDeliveryDoesNotExist() {
        when(deliveryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(DeliveryNotFoundException.class);
    }
}
