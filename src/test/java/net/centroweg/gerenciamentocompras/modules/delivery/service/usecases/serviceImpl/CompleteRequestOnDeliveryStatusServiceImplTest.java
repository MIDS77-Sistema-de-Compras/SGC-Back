package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;

import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.cancelledStatus;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.deliveredStatus;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.delivery;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.request;
import static net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.DeliveryServiceTestFixtures.status;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CompleteRequestOnDeliveryStatusServiceImplTest {

    @Mock private RequestPublicApi requestPublicApi;

    private CompleteRequestOnDeliveryStatusServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CompleteRequestOnDeliveryStatusServiceImpl(requestPublicApi);
    }

    @Test
    @DisplayName("Deve concluir a solicitação quando o status da entrega é Entregue")
    void shouldConcludeRequestWhenStatusIsEntregue() {
        Delivery delivery = delivery(request(), deliveredStatus());

        service.apply(delivery);

        verify(requestPublicApi).concludeRequest(delivery.getRequest().getId(), "ENTREGUE");
    }

    @Test
    @DisplayName("Deve concluir a solicitação quando o status da entrega é Pedido cancelado, ignorando caixa")
    void shouldConcludeRequestWhenStatusIsPedidoCanceladoCaseInsensitive() {
        Status mixedCaseCancelled = cancelledStatus();
        mixedCaseCancelled.setName("pedido CANCELADO");
        Delivery delivery = delivery(request(), mixedCaseCancelled);

        service.apply(delivery);

        verify(requestPublicApi).concludeRequest(delivery.getRequest().getId(), "pedido CANCELADO");
    }

    @Test
    @DisplayName("Não deve concluir a solicitação para qualquer outro status")
    void shouldNotConcludeRequestWhenStatusIsSomethingElse() {
        Delivery delivery = delivery(request(), status());

        service.apply(delivery);

        verifyNoInteractions(requestPublicApi);
    }

    @Test
    @DisplayName("Não deve lançar exceção quando o status da entrega for nulo")
    void shouldNotThrowWhenDeliveryStatusIsNull() {
        Delivery delivery = delivery(request(), status());
        delivery.setStatus(null);

        service.apply(delivery);

        verifyNoInteractions(requestPublicApi);
    }
}
