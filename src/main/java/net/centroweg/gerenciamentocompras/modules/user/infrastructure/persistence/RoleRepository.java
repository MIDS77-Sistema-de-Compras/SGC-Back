package net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByNameIgnoringCase(String name);
}
