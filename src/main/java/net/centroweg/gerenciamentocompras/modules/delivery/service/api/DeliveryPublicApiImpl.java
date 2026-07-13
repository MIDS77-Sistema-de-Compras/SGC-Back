package net.centroweg.gerenciamentocompras.modules.delivery.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryPublicApiImpl implements DeliveryPublicApi {

    private final DeliveryRepository deliveryRepository;

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
