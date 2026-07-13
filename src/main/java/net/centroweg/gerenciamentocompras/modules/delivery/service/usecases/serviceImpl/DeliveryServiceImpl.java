package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.ConfirmDeliveryReceiverRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.CreateDeliveryRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.UpdateDeliveryRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;
import net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceIntrf.DeliveryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final CreateDeliveryServiceImpl createDeliveryService;
    private final FindDeliveryByIdServiceImpl findDeliveryByIdService;
    private final FindAllDeliveryServiceImpl findAllDeliveryService;
    private final UpdateDeliveryServiceImpl updateDeliveryService;
    private final DeleteDeliveryServiceImpl deleteDeliveryService;
    private final ConfirmDeliveryReceiverServiceImpl confirmDeliveryReceiverService;

    @Override
    public DeliveryResponse create(CreateDeliveryRequest request) {
        return createDeliveryService.create(request);
    }

    @Override
    public DeliveryResponse findById(Long id) {
        return findDeliveryByIdService.findById(id);
    }

    @Override
    public List<DeliveryResponse> findAll(Boolean active, Long requestId, Long receiverId) {
        return findAllDeliveryService.findAll(active, requestId, receiverId);
    }

    @Override
    public DeliveryResponse update(Long id, UpdateDeliveryRequest request) {
        return updateDeliveryService.update(id, request);
    }

    @Override
    public void delete(Long id) {
        deleteDeliveryService.delete(id);
    }

    @Override
    public DeliveryResponse confirmReceiver(Long deliveryId, Long userId, ConfirmDeliveryReceiverRequest request) {
        return confirmDeliveryReceiverService.confirmReceiver(deliveryId, userId, request);
    }
}
