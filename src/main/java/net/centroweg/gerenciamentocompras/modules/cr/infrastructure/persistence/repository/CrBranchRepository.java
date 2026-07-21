package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import jakarta.persistence.LockModeType;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

    /**
     * Verifica se o usuário é responsável por algum vínculo cujo CR é master.
     *
     * @param userId identificador do usuário
     * @return {@code true} se o usuário for responsável por um CR Master
     */
    boolean existsByCrMasterTrueAndResponsibleUsersId(Long userId);

    /**
     * Carrega os CRs Master e todos os responsáveis para correção de dados legados.
     */
    @EntityGraph(attributePaths = {"cr", "responsibleUsers", "responsibleUsers.role"})
    @Query("select distinct crBranch from CrBranch crBranch where crBranch.cr.master = true")
    List<CrBranch> findAllMasterWithResponsibles();

    @EntityGraph(attributePaths = {"cr", "responsibleUsers", "responsibleUsers.role"})
    @Query("select distinct crBranch from CrBranch crBranch where crBranch.cr.id = :crId")
    List<CrBranch> findAllByCrIdWithResponsibles(@Param("crId") Long crId);

    /**
     * Busca todas as filiais pelas quais o usuário é responsável, carregando a lista completa
     * de responsáveis para evitar consultas repetidas durante a criação da delegação.
     */
    @EntityGraph(attributePaths = "responsibleUsers")
    @Query("""
            select distinct crBranch
            from CrBranch crBranch
            join crBranch.responsibleUsers responsible
            where responsible.id = :userId
            """)
    List<CrBranch> findAllByResponsibleUserId(@Param("userId") Long userId);

    /**
     * Bloqueia as filiais antes de alterar responsáveis, serializando delegações concorrentes.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select crBranch from CrBranch crBranch where crBranch.id in :ids order by crBranch.id")
    List<CrBranch> findAllByIdForUpdate(@Param("ids") Collection<Long> ids);

}
