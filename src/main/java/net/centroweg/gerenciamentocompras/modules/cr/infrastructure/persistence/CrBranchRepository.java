package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface CrBranchRepository extends JpaRepository<CrBranch, Long> {

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

    /**
     * Busca um vínculo pelo seu identificador, apenas quando houver um usuário responsável definido.
     *
     * @param id
     * @return um {@link Optional} com o vínculo, caso exista e possua responsável
     */
    Optional<CrBranch> findByIdAndResponsibleUserIsNotNull(Long id);
}
