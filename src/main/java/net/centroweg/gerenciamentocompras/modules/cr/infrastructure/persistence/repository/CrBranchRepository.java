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
     * Busca todos os vínculos CR-filial pertencentes a uma filial.
     * @param branchId ID da filial.
     * @return lista de vínculos encontrados, caso exista.
     */
    List<CrBranch> findByBranchId(Long branchId);

    /**
     * Busca um vínculo específico pela combinação de CR e filial.
     * @param crId ID da CR.
     * @param branchId ID da filial.
     * @return vínculo encontrado, caso exista.
     */
    Optional<CrBranch> findByCrIdAndBranchId(Long crId, Long branchId);
}
