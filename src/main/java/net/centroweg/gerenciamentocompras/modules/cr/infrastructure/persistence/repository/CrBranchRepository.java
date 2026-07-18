package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositório de acesso a dados da entidade {@link CrBranch}.
 */
@Repository
public interface CrBranchRepository extends JpaRepository<CrBranch, Long>, JpaSpecificationExecutor<CrBranch> {

    /**
     * Lista todos os vínculos CR-filial pertencentes a filial informada cadastrados no banco de dados.
     * @param branchId identificador da filial.
     * @return lista todos os vínculos encontrados, caso exista.
     */
    List<CrBranch> findByBranchId(Long branchId);

    /**
     * Busca um vínculo específico no banco de dados pela combinação de CR e filial informados.
     * @param crId identificador da CR.
     * @param branchId identificador da filial.
     * @return vínculo encontrado, caso exista.
     */
    Optional<CrBranch> findByCrIdAndBranchId(Long crId, Long branchId);
}
