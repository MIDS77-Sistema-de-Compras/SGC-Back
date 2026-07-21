package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRequestProductRepository extends JpaRepository<ItemRequestProduct, Long> {

    boolean existsByRequestIdAndProductId(Long requestId, Long productId);

    boolean existsByRequestIdAndProductIdAndIdNot(Long requestId, Long productId, Long id);
}
