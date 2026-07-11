package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> , JpaSpecificationExecutor<Request> {

    List<Request> findByActiveTrue();

    @Query("""
        SELECT r FROM Request r
        WHERE r.active = true
          AND LOWER(r.status.name) = LOWER(:statusName)
          AND r.requestDate < :limite
    """)
    List<Request> findPendentesAntigas(@Param("statusName") String statusName,
                                       @Param("limite") LocalDateTime limite);
}

