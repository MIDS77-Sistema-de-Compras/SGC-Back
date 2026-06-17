package net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence;

import java.util.Optional;

import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;

public interface MeasurementUnitRepository extends JpaRepository <MeasurementUnit, Long>{

    Optional<MeasurementUnit> findByAbbreviation(String abbreviation);
    Optional<MeasurementUnit> findByNameIgnoreCase(String name);
    
}
