package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import org.springframework.stereotype.Service;

/**
 * Verifica se o status atual de uma entrega é "Entregue" ou "Pedido cancelado"
 * e, em caso positivo, conclui a solicitação vinculada via {@link RequestPublicApi}.
 */
@Service
@RequiredArgsConstructor
public class CompleteRequestOnDeliveryStatusServiceImpl {

    private static final String DELIVERED_STATUS = "Entregue";
    private static final String CANCELLED_STATUS = "Pedido cancelado";

    private final RequestPublicApi requestPublicApi;

    public void apply(Delivery delivery) {
        String statusName = delivery.getStatus() != null ? delivery.getStatus().getName() : null;
        if (isCompletionStatus(statusName)) {
            requestPublicApi.concludeRequest(delivery.getRequest().getId());
        }
    }

    private boolean isCompletionStatus(String statusName) {
        return statusName != null
                && (statusName.trim().equalsIgnoreCase(DELIVERED_STATUS)
                    || statusName.trim().equalsIgnoreCase(CANCELLED_STATUS));
    }
}
