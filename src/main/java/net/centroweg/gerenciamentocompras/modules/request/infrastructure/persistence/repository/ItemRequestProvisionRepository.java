package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

@Repository
public interface ItemRequestProvisionRepository extends JpaRepository<ItemRequestProvision, Long> {
    List<ItemRequestProvision> findAllByRequestId(@Param("requestId") Long requestId);
    Optional<ItemRequestProvision> findByIdAndRequestId(Long id, @Param("requestId") Long requestId);
}
