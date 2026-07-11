package net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Override
    @EntityGraph(attributePaths = {"request", "status", "receivers", "receivers.user"})
    Optional<Delivery> findById(Long id);

    @EntityGraph(attributePaths = {"request", "status", "receivers", "receivers.user"})
    List<Delivery> findByActiveTrue();

    @EntityGraph(attributePaths = {"request", "status", "receivers", "receivers.user"})
    List<Delivery> findByRequestId(Long requestId);

    @EntityGraph(attributePaths = {"request", "status", "receivers", "receivers.user"})
    @Query("select distinct delivery from Delivery delivery join delivery.receivers receiver where receiver.user.id = :receiverId")
    List<Delivery> findByReceiverId(@Param("receiverId") Long receiverId);

    @EntityGraph(attributePaths = {"request", "status", "receivers", "receivers.user"})
    @Query("select distinct delivery from Delivery delivery join delivery.productItems item where delivery.request.id = :requestId and item.id = :itemId")
    List<Delivery> findByRequestIdAndProductItemId(@Param("requestId") Long requestId, @Param("itemId") Long itemId);

    @EntityGraph(attributePaths = {"request", "status", "receivers", "receivers.user"})
    @Query("select distinct delivery from Delivery delivery join delivery.provisionItems item where delivery.request.id = :requestId and item.id = :itemId")
    List<Delivery> findByRequestIdAndProvisionItemId(@Param("requestId") Long requestId, @Param("itemId") Long itemId);
}
