package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório de acesso a dados da entidade {@link Request}.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> , JpaSpecificationExecutor<Request> {

    /**
     * Lista todas as requisições cadastradas no banco de dados atualmente ativas.
     * @return lista com todas as requisições encontradas, caso exista.
     */
    List<Request> findByActiveTrue();
}
