package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryAlreadyInactiveException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteDeliveryServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private StatusRepository statusRepository;

    private DeleteDeliveryServiceImpl service;
    private Delivery delivery;

    @BeforeEach
    void setUp() {
        service = new DeleteDeliveryServiceImpl(deliveryRepository, statusRepository);
        Request request = request();
        Status status = status();
        User firstReceiver = user(1L, "Primeiro", true);
        User secondReceiver = user(2L, "Segundo", true);
        delivery = delivery(request, status, firstReceiver, secondReceiver);
    }

    @Test
    void shouldInactivateDeliveryAndApplyCancelledStatus() {
        Status cancelled = new Status("Pedido cancelado", "A entrega foi cancelada pelo comprador.");
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));
        when(statusRepository.findByNameIgnoreCase("Pedido cancelado")).thenReturn(Optional.of(cancelled));

        service.delete(100L);

        assertThat(delivery.getActive()).isFalse();
        assertThat(delivery.getStatus()).isSameAs(cancelled);
        verify(deliveryRepository).save(delivery);
    }

    @Test
    void shouldKeepStatusWhenCancelledStatusDoesNotExist() {
        Status original = delivery.getStatus();
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));
        when(statusRepository.findByNameIgnoreCase("Pedido cancelado")).thenReturn(Optional.empty());

        service.delete(100L);

        assertThat(delivery.getActive()).isFalse();
        assertThat(delivery.getStatus()).isSameAs(original);
        verify(deliveryRepository).save(delivery);
    }

    @Test
    void shouldRejectSecondInactivation() {
        delivery.setActive(false);
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));

        assertThatThrownBy(() -> service.delete(100L))
                .isInstanceOf(DeliveryAlreadyInactiveException.class);
    }
}
