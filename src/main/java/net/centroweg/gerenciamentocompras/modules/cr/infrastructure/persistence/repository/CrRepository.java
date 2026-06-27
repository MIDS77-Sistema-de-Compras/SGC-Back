package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório de acesso a dados da entidade {@link Cr}.
 */
@Repository
public interface CrRepository extends JpaRepository<Cr, Long> {
}
