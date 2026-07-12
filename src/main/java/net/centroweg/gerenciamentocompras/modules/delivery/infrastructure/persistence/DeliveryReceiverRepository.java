package net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.DeliveryReceiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryReceiverRepository extends JpaRepository<DeliveryReceiver, Long> {
}
