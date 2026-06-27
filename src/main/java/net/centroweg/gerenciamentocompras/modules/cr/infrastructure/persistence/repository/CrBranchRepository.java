package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de acesso a dados para a entidade {@link CrBranch}.
 */

@Repository
public interface CrBranchRepository extends JpaRepository<CrBranch, Long>, JpaSpecificationExecutor<CrBranch> {

    /**
     * Busca todos os vínculos CR-Branch pertencentes a uma Branch.
     * @param branchId ID da Branch vinculada ao CR-Branch.
     * @return a lista de vínculos encontrados, caso não tenha volta nula.
     */
    List<CrBranch> findByBranchId(Long branchId);

    /**
     * Busca um vínculo específico pela combinação de CR e Branch.
     * @param crId ID da CR vinculada ao CR-Branch.
     * @param branchId ID da Branch vinculada ao CR-Branch.
     * @return {@link Optional} com o vínculo, caso exista
     */
    Optional<CrBranch> findByCrIdAndBranchId(Long crId, Long branchId);

}
