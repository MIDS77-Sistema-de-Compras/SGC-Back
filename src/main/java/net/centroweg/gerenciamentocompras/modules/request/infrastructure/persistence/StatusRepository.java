package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    boolean existsByName (String name);

}
