package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.confirmRequest;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.createRequest;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.response;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.updateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplTest {

    @Mock
    private CreateDeliveryServiceImpl createDeliveryService;

    @Mock
    private FindDeliveryByIdServiceImpl findDeliveryByIdService;

    @Mock
    private FindAllDeliveryServiceImpl findAllDeliveryService;

    @Mock
    private UpdateDeliveryServiceImpl updateDeliveryService;

    @Mock
    private DeleteDeliveryServiceImpl deleteDeliveryService;

    @Mock
    private ConfirmDeliveryReceiverServiceImpl confirmDeliveryReceiverService;

    private DeliveryServiceImpl service;
    private DeliveryResponse response;

    @BeforeEach
    void setUp() {
        service = new DeliveryServiceImpl(
                createDeliveryService,
                findDeliveryByIdService,
                findAllDeliveryService,
                updateDeliveryService,
                deleteDeliveryService,
                confirmDeliveryReceiverService
        );
        response = response();
    }

    @Test
    void shouldDelegateCreate() {
        var request = createRequest(List.of(1L, 2L));
        when(createDeliveryService.create(request)).thenReturn(response);

        assertThat(service.create(request)).isSameAs(response);
    }

    @Test
    void shouldDelegateFindById() {
        when(findDeliveryByIdService.findById(100L)).thenReturn(response);

        assertThat(service.findById(100L)).isSameAs(response);
    }

    @Test
    void shouldDelegateFindAll() {
        when(findAllDeliveryService.findAll(true, 10L, 1L)).thenReturn(List.of(response));

        assertThat(service.findAll(true, 10L, 1L)).containsExactly(response);
    }

    @Test
    void shouldDelegateUpdate() {
        var request = updateRequest(List.of(1L, 2L));
        when(updateDeliveryService.update(100L, request)).thenReturn(response);

        assertThat(service.update(100L, request)).isSameAs(response);
    }

    @Test
    void shouldDelegateDelete() {
        service.delete(100L);

        verify(deleteDeliveryService).delete(100L);
    }

    @Test
    void shouldDelegateConfirmReceiver() {
        var request = confirmRequest();
        when(confirmDeliveryReceiverService.confirmReceiver(100L, 1L, request)).thenReturn(response);

        assertThat(service.confirmReceiver(100L, 1L, request)).isSameAs(response);
    }
}
