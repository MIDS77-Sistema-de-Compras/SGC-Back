package net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.DeliveryReceiver;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.DeliveryReceiverId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryReceiverRepository extends JpaRepository<DeliveryReceiver, DeliveryReceiverId> {

    Optional<DeliveryReceiver> findByIdDeliveryIdAndIdUserId(Long deliveryId, Long userId);
}
