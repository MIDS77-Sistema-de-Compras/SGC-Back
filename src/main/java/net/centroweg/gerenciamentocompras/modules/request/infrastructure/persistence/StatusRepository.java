package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    boolean existsByName (String name);
    Optional<Status> findByName(String name);

}
