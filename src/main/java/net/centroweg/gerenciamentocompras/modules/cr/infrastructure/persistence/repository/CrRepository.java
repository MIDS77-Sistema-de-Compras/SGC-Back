package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data JPA para a entidade {@link Cr}.
 * Fornece operações CRUD e de paginação herdadas de {@link JpaRepository}.
 */
@Repository
public interface CrRepository extends JpaRepository<Cr, Long> {
}
