package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> , JpaSpecificationExecutor<Request> {

    List<Request> findByActiveTrue();
}
