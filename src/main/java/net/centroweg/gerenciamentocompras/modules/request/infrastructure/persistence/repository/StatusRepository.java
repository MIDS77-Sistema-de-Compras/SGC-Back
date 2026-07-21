package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório de acesso a dados da entidade {@link Status}.
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    /**
     * Busca um status no banco de dados pelo nome informado, sem distinção entre maiúsculas e minúsculas.
     * @param name nome do status.
     * @return status encontrado, caso exista.
     */
    Optional<Status> findByNameIgnoreCase(String name);

    /**
     * Verifica se já existe um status cadastrado no banco de dados com o nome informado.
     * @param name nome do status.
     * @return booleano
     */
    Boolean existsByName (String name);

}
