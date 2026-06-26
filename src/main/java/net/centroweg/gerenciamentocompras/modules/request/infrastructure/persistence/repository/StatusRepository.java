package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Interface responsável pelo acesso e manipulação
 * dos dados da entidade {@link Status}.
 *
 * <p>Utiliza o Spring Data JPA para fornecer operações
 * de persistência no banco de dados.</p>
 *
 * @author André
 * @since 1.0
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    Optional<Status> findByNameIgnoreCase(String name);
    /**
     * Verifica se já existe um status cadastrado
     * com o nome informado.
     *
     * @param name nome do status
     * @return {@code true} caso exista um status com o nome informado,
     *         caso contrário {@code false}
     */
    Boolean existsByName (String name);

}
