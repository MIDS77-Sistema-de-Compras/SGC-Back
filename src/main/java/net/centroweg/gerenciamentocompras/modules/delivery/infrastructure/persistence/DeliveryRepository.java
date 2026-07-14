package net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryProductNotificationData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Override
    @EntityGraph(attributePaths = {"request", "receivers", "receivers.user"})
    Optional<Delivery> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select delivery from Delivery delivery where delivery.id = :id")
    Optional<Delivery> findByIdForUpdate(@Param("id") Long id);

    @EntityGraph(attributePaths = {"request", "receivers", "receivers.user"})
    List<Delivery> findByActiveTrue();

    @EntityGraph(attributePaths = {"request", "receivers", "receivers.user"})
    List<Delivery> findByRequestId(Long requestId);

    @EntityGraph(attributePaths = {"request", "receivers", "receivers.user"})
    @Query("select distinct delivery from Delivery delivery join delivery.receivers receiver where receiver.user.id = :receiverId")
    List<Delivery> findByReceiverId(@Param("receiverId") Long receiverId);

    @Query("select distinct delivery from Delivery delivery join delivery.productItems item " +
            "where delivery.request.id = :requestId and item.id = :itemId and delivery.active = true")
    List<Delivery> findActiveByRequestIdAndProductItemId(
            @Param("requestId") Long requestId,
            @Param("itemId") Long itemId
    );

    @Query("select distinct delivery from Delivery delivery join delivery.provisionItems item " +
            "where delivery.request.id = :requestId and item.id = :itemId and delivery.active = true")
    List<Delivery> findActiveByRequestIdAndProvisionItemId(
            @Param("requestId") Long requestId,
            @Param("itemId") Long itemId
    );

    @Query("select new net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryProductNotificationData(" +
            "item.product.name, item.product.code, item.quantity, item.measurementUnit.name) " +
            "from Delivery delivery join delivery.productItems item where delivery.id = :deliveryId")
    List<DeliveryProductNotificationData> findProductNotificationData(@Param("deliveryId") Long deliveryId);

    @Query("select item.provision.name from Delivery delivery join delivery.provisionItems item " +
            "where delivery.id = :deliveryId")
    List<String> findProvisionNotificationNames(@Param("deliveryId") Long deliveryId);

    @Query("select distinct receiver.user.name from DeliveryReceiver receiver " +
            "where receiver.delivery.id = :deliveryId")
    List<String> findReceiverNotificationNames(@Param("deliveryId") Long deliveryId);
}
