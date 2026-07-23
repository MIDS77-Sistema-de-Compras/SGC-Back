package net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositório de acesso a dados da entidade {@link Role}.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca um nível de acesso pelo nome, ignorando maiúsculas e minúsculas.
     * @param name nome do nível de acesso.
     * @return o nível de acesso encontrado, caso exista.
     */
    Optional<Role> findByNameIgnoreCase(String name);

    /**
     * Lista todos os níveis de acesso cadastrados no banco de dados pelo nome, ignorando maiúsculas e minúsculas.
     * @param name nome do nível de acesso.
     * @return lista com todos os níveis de acesso encontrados, caso exista.
     */
    List<Role> findByNameIgnoringCase(String name);
}
