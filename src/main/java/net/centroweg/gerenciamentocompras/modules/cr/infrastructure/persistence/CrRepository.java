package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.cr.domain.Cr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrRepository extends JpaRepository<Cr, Long> {
}
