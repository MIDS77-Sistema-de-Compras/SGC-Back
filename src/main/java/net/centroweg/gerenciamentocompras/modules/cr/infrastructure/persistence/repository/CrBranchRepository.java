package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de acesso a dados para a entidade {@link CrBranch}.
 *
 * <p>Estende {@link JpaRepository}, herdando as operações de CRUD padrão,
 * e define consultas derivadas específicas para os vínculos entre CR e filial.</p>
 */

@Repository
public interface CrBranchRepository extends JpaRepository<CrBranch, Long>, JpaSpecificationExecutor<CrBranch> {

    /**
     * Busca todos os vínculos CR-filial pertencentes a uma filial.
     *
     * @param branchId
     * @return a lista de vínculos encontrados (vazia se não houver nenhum)
     */
    List<CrBranch> findByBranchId(Long branchId);

    /**
     * Busca um vínculo específico pela combinação de CR e filial.
     *
     * @param crId
     * @param branchId
     * @return um {@link Optional} com o vínculo, caso exista
     */
    Optional<CrBranch> findByCrIdAndBranchId(Long crId, Long branchId);

}
