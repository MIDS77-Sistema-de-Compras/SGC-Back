package net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;

/**
 * Repositório de acesso a dados da entidade {@link Provision}.
 */
@Repository
public interface ProvisionRepository extends JpaRepository<Provision, Long> {}
