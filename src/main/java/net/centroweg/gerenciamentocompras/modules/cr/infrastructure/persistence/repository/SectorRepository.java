package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório de acesso a dados da entidade {@link Sector}.
 */
@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

    /**
     * Busca um bloco pelo nome.
     * @param name nome do bloco.
     * @return {@link Optional} com o bloco encontrado, caso exista.
     */
    Optional<Sector> findByName(String name);
}
