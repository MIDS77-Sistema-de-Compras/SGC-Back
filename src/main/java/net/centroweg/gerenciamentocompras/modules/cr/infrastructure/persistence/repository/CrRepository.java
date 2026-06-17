package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrRepository extends JpaRepository<Cr, Long> {
}
