package net.centroweg.gerenciamentocompras.modules.delivery.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationData;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryCreatedNotificationData;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationRecipient;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryStatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.service.api.StatusPublicApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryPublicApiImpl implements DeliveryPublicApi {

    private final DeliveryRepository deliveryRepository;
    private final StatusPublicApi statusPublicApi;

    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryNotificationData> findActiveDeliveryByProductItem(Long requestId, Long itemId) {
        return firstDelivery(deliveryRepository.findActiveByRequestIdAndProductItemId(requestId, itemId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryNotificationData> findActiveDeliveryByProvisionItem(Long requestId, Long itemId) {
        return firstDelivery(deliveryRepository.findActiveByRequestIdAndProvisionItemId(requestId, itemId));
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryCreatedNotificationData findNotificationDataById(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(DeliveryNotFoundException::new);
        var status = statusPublicApi.findById(delivery.getStatusId()).orElseThrow(DeliveryStatusNotFoundException::new);
        return new DeliveryCreatedNotificationData(
                delivery.getId(), delivery.getRequest().getId(), status.id(), status.name(),
                delivery.getExpectedDeliveryAt(), delivery.getDeliveredAt(), delivery.getDeliveryLocation(),
                delivery.getDescription(),
                delivery.getReceivers().stream()
                        .map(receiver -> new DeliveryNotificationRecipient(receiver.getUser().getId(),
                                receiver.getUser().getName(), receiver.getUser().getEmail())).toList(),
                deliveryRepository.findProductNotificationData(deliveryId),
                deliveryRepository.findProvisionNotificationNames(deliveryId)
        );
    }

    private Optional<DeliveryNotificationData> firstDelivery(List<Delivery> deliveries) {
        return deliveries.stream().findFirst().map(this::toNotificationData);
    }

    private DeliveryNotificationData toNotificationData(Delivery delivery) {
        return new DeliveryNotificationData(
                delivery.getId(),
                delivery.getDeliveredAt(),
                delivery.getDeliveryLocation(),
                delivery.getDescription(),
                deliveryRepository.findReceiverNotificationNames(delivery.getId()),
                deliveryRepository.findProductNotificationData(delivery.getId()),
                deliveryRepository.findProvisionNotificationNames(delivery.getId())
        );
    }
}
