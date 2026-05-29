package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrBranchRepository extends JpaRepository<CrBranch, Long> {
    List<CrBranch> findByBranchId(Long branchId);
    Optional<CrBranch> findByCrIdAndBranchId(Long crId, Long branchId);
    Optional<CrBranch> findByIdAndResponsibleUserIsNotNull(Long id);
}
