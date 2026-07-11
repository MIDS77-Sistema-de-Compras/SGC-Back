package net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;

/**
 * Repositório de acesso a dados da entidade {@link MeasurementUnit}.
 */
public interface MeasurementUnitRepository
        extends JpaRepository<MeasurementUnit, Long> {

    /**
     * Busca uma unidade de medida pela abreviação.
     * @param abbreviation sigla da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     */
    Optional<MeasurementUnit> findByAbbreviation(String abbreviation);

    /**
     * Busca uma unidade de medida pelo nome.
     * @param name nome da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     */
    Optional<MeasurementUnit> findByNameIgnoreCase(String name);
    
}
