package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryStatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.StatusPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.util.RequestStatusNames;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Caso de uso responsável por criar automaticamente a entrega
 * de uma solicitação aprovada.
 *
 * <p>A entrega nasce com o status "Em atendimento", vinculada a todos
 * os itens da solicitação. Local e data prevista recebem valores padrão
 * e devem ser ajustados pelo comprador ao dar andamento na compra.</p>
 */
@Service
@RequiredArgsConstructor
public class CreateDeliveryForApprovedRequestServiceImpl {

    private static final String IN_SERVICE_STATUS = RequestStatusNames.EM_ATENDIMENTO;
    private static final String DEFAULT_DELIVERY_LOCATION = "A definir";
    private static final String DEFAULT_DESCRIPTION =
            "Entrega criada automaticamente após a aprovação da solicitação.";
    private static final int DEFAULT_EXPECTED_DAYS = 7;

    private final DeliveryRepository deliveryRepository;
    private final RequestPublicApi requestPublicApi;
    private final StatusPublicApi statusPublicApi;

    /**
     * Cria a entrega da solicitação aprovada, caso ainda não exista nenhuma.
     *
     * @param requestId identificador da solicitação aprovada
     */
    @Transactional
    public void createForRequest(Long requestId) {
        if (!deliveryRepository.findByRequestId(requestId).isEmpty()) {
            return;
        }

        Request request = requestPublicApi.findRequestById(requestId)
                .orElseThrow(RequestNotFoundException::new);

        Status status = statusPublicApi.findByName(IN_SERVICE_STATUS)
                .orElseThrow(DeliveryStatusNotFoundException::new);

        Delivery delivery = new Delivery();
        delivery.setRequest(request);
        delivery.setStatus(status);
        delivery.setExpectedDeliveryAt(LocalDateTime.now().plusDays(DEFAULT_EXPECTED_DAYS));
        delivery.setDeliveryLocation(DEFAULT_DELIVERY_LOCATION);
        delivery.setDescription(DEFAULT_DESCRIPTION);
        delivery.getProductItems().addAll(request.getItemRequestProducts());
        delivery.getProvisionItems().addAll(request.getItemRequestProvisions());

        deliveryRepository.save(delivery);
    }
}
