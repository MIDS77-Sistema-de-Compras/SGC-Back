package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório de acesso a dados da entidade {@link ItemRequestProduct}.
 */
@Repository
public interface ItemRequestProductRepository extends JpaRepository<ItemRequestProduct, Long> {
}
