package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * Repositório de acesso a dados da entidade {@link ItemRequestProvision}.
 */
@Repository
public interface ItemRequestProvisionRepository extends JpaRepository<ItemRequestProvision, Long> {

    /**
     * Lista todos os itens de serviço cadastrados no banco de dados associados a uma requisição específica.
     * @param requestId identificador da requisição.
     * @return lista com todos os itens encontrados, caso exista.
     */
    List<ItemRequestProvision> findAllByRequestId(@Param("requestId") Long requestId);

    /**
     * Busca um item de serviço no banco de dados pelo seu identificador, garantindo que pertença à requisição informada.
     * @param id identificador do item.
     * @param requestId identificador da requisição.
     * @return item encontrado, caso exista e pertença à requisição informada.
     */
    Optional<ItemRequestProvision> findByIdAndRequestId(Long id, @Param("requestId") Long requestId);
}
