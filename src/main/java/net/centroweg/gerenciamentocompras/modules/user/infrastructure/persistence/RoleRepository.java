package net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Classe de interação com o DB */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNameIgnoreCase(String name);

    /**
     * Lista as roles contendo o nome pesquisado
     * @param name
     * @return uma lista de roles
     */
    List<Role> findByNameIgnoringCase(String name);
}
