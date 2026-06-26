package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para a entidade {@link Branch}.
 *
 * <p>Fornece operações CRUD padrão herdadas de {@link JpaRepository},
 * como {@code save}, {@code findById}, {@code findAll} e {@code deleteById}.</p>
 *
 * @author Leandro
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

}
