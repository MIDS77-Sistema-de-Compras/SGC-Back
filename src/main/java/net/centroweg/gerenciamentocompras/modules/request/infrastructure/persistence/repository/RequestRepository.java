package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> , JpaSpecificationExecutor<Request> {

    List<Request> findByActiveTrue();

    @EntityGraph(attributePaths = {"createdByUsers"})
    @Query("select request from Request request where request.id = :id")
    Optional<Request> findWithRequestersById(@Param("id") Long id);

    @EntityGraph(attributePaths = {
            "createdByUsers",
            "crBranch",
            "crBranch.cr",
            "crBranch.branch",
            "status"
    })
    @Query("select request from Request request where request.id = :id")
    Optional<Request> findForStatusNotificationById(@Param("id") Long id);

    @EntityGraph(attributePaths = {
            "createdByUsers", "crBranch", "crBranch.cr", "crBranch.branch", "status"
    })
    @Query("select request from Request request where request.id = :id")
    Optional<Request> findForEmailNotificationById(@Param("id") Long id);

    // ---------------------------------------------------------------------
    // Pré-carregamento das associações usadas ao montar RequestResponse.
    //
    // Cada coleção é buscada em sua PRÓPRIA query (por lista de ids), porque
    // Request tem três coleções do tipo "bag" (produtos, serviços, anexos) e
    // buscar mais de uma no mesmo JOIN FETCH lançaria MultipleBagFetchException.
    // Executadas na mesma transação da listagem, elas inicializam as entidades
    // já gerenciadas — o mapper depois lê tudo sem novas idas ao banco.
    // ---------------------------------------------------------------------

    @Query("""
            select distinct request from Request request
            left join fetch request.itemRequestProducts item
            left join fetch item.product
            left join fetch item.measurementUnit
            left join fetch item.status_id
            where request.id in :ids
            """)
    List<Request> fetchProductItems(@Param("ids") List<Long> ids);

    @Query("""
            select distinct request from Request request
            left join fetch request.itemRequestProvisions item
            left join fetch item.provision
            left join fetch item.status
            where request.id in :ids
            """)
    List<Request> fetchProvisionItems(@Param("ids") List<Long> ids);

    @Query("""
            select distinct request from Request request
            left join fetch request.attachments
            where request.id in :ids
            """)
    List<Request> fetchAttachments(@Param("ids") List<Long> ids);

    @Query("""
            select distinct request from Request request
            left join fetch request.createdByUsers
            where request.id in :ids
            """)
    List<Request> fetchRequesters(@Param("ids") List<Long> ids);

    /**
     * Inicializa, em poucas consultas, todas as associações que o
     * {@code RequestMapper} percorre ao montar a resposta COMPLETA de uma listagem
     * (usada onde a resposta expõe itens, anexos e solicitantes). Sem isso, o lazy
     * loading por lote dispara muitas idas ao banco — caro quando o banco é remoto.
     *
     * @param ids ids das solicitações da página atual
     */
    default void initializeForResponse(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        fetchProductItems(ids);
        fetchProvisionItems(ids);
        fetchAttachments(ids);
        fetchRequesters(ids);
    }

    @Query("""
            select distinct request from Request request
            left join fetch request.crBranch crBranch
            left join fetch crBranch.cr
            where request.id in :ids
            """)
    List<Request> fetchCrBranches(@Param("ids") List<Long> ids);

    @Query("""
            select distinct request from Request request
            left join fetch request.itemRequestProducts item
            left join fetch item.product
            where request.id in :ids
            """)
    List<Request> fetchProductNames(@Param("ids") List<Long> ids);

    /**
     * Inicializa só o necessário para a resposta ENXUTA de listagem
     * ({@code RequestListItemResponse}): o CR (para o código) e os produtos
     * (para nome e contagem). Não toca em itens de serviço, anexos nem solicitantes.
     *
     * @param ids ids das solicitações da página atual
     */
    default void initializeForListResponse(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        fetchCrBranches(ids);
        fetchProductNames(ids);
    }
}
