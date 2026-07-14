package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryAlreadyInactiveException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.api.StatusPublicApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteDeliveryServiceImpl {

    private static final String CANCELLED_STATUS = "Pedido cancelado";

    private final DeliveryRepository deliveryRepository;
    private final StatusPublicApi statusPublicApi;

    @Transactional
    public void delete(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(DeliveryNotFoundException::new);
        ensureActive(delivery);

        delivery.setActive(false);
        applyCancelledStatus(delivery);
        deliveryRepository.save(delivery);
    }

    private void ensureActive(Delivery delivery) {
        if (!Boolean.TRUE.equals(delivery.getActive())) {
            throw new DeliveryAlreadyInactiveException();
        }
    }

    /**
     * Marca a entrega cancelada com o status "Pedido cancelado".
     */
    private void applyCancelledStatus(Delivery delivery) {
        statusPublicApi.findByName(CANCELLED_STATUS)
                .ifPresent(delivery::setStatus);
    }
}
