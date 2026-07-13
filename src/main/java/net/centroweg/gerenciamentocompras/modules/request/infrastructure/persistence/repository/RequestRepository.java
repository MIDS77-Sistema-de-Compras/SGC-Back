package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> , JpaSpecificationExecutor<Request> {

    List<Request> findByActiveTrue();

    @EntityGraph(attributePaths = {"createdByUsers"})
    @Query("select request from Request request where request.id = :id")
    Optional<Request> findWithRequestersById(@Param("id") Long id);

    @EntityGraph(attributePaths = {
            "createdByUsers",
            "crBranch",
            "crBranch.cr",
            "crBranch.branch",
            "status"
    })
    @Query("select request from Request request where request.id = :id")
    Optional<Request> findForStatusNotificationById(@Param("id") Long id);
}
