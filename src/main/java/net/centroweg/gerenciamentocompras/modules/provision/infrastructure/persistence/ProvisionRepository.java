package net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;

/**
 * Interface extendendo o JPA para interação no banco de dados.
 * @see {@code JpaRepository<T,ID>}
 */
@Repository
public interface ProvisionRepository extends JpaRepository<Provision, Long> {
    Optional<Provision> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
