package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório de acesso a dados da entidade {@link Branch}.
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
}
