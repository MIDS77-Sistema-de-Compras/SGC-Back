package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.ConfirmDeliveryReceiverRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.CreateDeliveryRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.UpdateDeliveryRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;

import java.util.List;

public interface DeliveryService {

    DeliveryResponse create(CreateDeliveryRequest request);

    DeliveryResponse findById(Long id);

    List<DeliveryResponse> findAll(Boolean active, Long requestId, Long receiverId);

    DeliveryResponse update(Long id, UpdateDeliveryRequest request);

    void delete(Long id);

    DeliveryResponse confirmReceiver(Long deliveryId, Long userId, ConfirmDeliveryReceiverRequest request);
}
