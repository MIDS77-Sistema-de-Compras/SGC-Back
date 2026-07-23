package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.util.RequestStatusNames;
import org.springframework.stereotype.Service;

/**
 * Verifica se o status atual de uma entrega é "Entregue" ou "Pedido cancelado"
 * e, em caso positivo, conclui a solicitação vinculada via {@link RequestPublicApi}.
 */
@Service
@RequiredArgsConstructor
public class CompleteRequestOnDeliveryStatusServiceImpl {

    private static final String DELIVERED_STATUS = RequestStatusNames.ENTREGUE;
    private static final String CANCELLED_STATUS = RequestStatusNames.PEDIDO_CANCELADO;

    private final RequestPublicApi requestPublicApi;

    public void apply(Delivery delivery) {
        String statusName = delivery.getStatus() != null ? delivery.getStatus().getName() : null;
        if (isCompletionStatus(statusName)) {
            requestPublicApi.concludeRequest(delivery.getRequest().getId(), statusName);
        }
    }

    private boolean isCompletionStatus(String statusName) {
        String normalizedStatus = RequestStatusNames.normalize(statusName);
        return normalizedStatus.equals(RequestStatusNames.normalize(DELIVERED_STATUS))
                || normalizedStatus.equals(RequestStatusNames.normalize(CANCELLED_STATUS));
    }
}
