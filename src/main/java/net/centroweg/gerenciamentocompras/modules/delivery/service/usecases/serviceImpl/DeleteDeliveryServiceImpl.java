package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryAlreadyInactiveException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.strategy.DeliveryCancelledStatusImpl;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteDeliveryServiceImpl {

    private final DeliveryRepository deliveryRepository;
    private final StatusRepository statusRepository;

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
        String cancelledStatusName = new DeliveryCancelledStatusImpl().getName();
        statusRepository.findByNameIgnoreCase(cancelledStatusName)
                .ifPresent(delivery::setStatus);
    }
}
