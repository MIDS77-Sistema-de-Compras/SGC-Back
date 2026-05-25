package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.cr.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {


}
