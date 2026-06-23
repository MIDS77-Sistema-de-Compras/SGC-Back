package net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence;

import java.util.Optional;

import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade MeasurementUnit.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */
public interface MeasurementUnitRepository
        extends JpaRepository<MeasurementUnit, Long> {

    /**
     * Busca uma unidade de medida pela abreviação.
     *
     * @param abbreviation Sigla da unidade de medida.
     * @return Unidade de medida encontrada, caso exista.
     */
    Optional<MeasurementUnit> findByAbbreviation(String abbreviation);
    Optional<MeasurementUnit> findByNameIgnoreCase(String name);
    
}
