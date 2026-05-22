package net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
