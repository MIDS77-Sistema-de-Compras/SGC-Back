package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByActiveTrue();
}
