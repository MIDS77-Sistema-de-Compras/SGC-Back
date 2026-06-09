package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

@Repository
public interface ItemRequestProvisionRepository extends JpaRepository<ItemRequestProvision, Long> {
    
}
