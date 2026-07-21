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
     * Busca uma unidade de medida no banco de dados pela abreviação informada.
     * @param abbreviation sigla da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     */
    Optional<MeasurementUnit> findByAbbreviation(String abbreviation);

    /**
     * Busca uma unidade de medida no banco de dados pelo nome informado.
     * @param name nome da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     */
    Optional<MeasurementUnit> findByNameIgnoreCase(String name);
    
}
