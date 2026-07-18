package net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.measurementUnit;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

/**
 * Caso de uso responsável pela listagem de todas as {@link MeasurementUnit} cadastradas.
 */
@Service
@RequiredArgsConstructor
public class FindAllMeasurementUnit {

    private final MeasurementUnitRepository measurementUnitRepository;
    private final MeasurementUnitMapper measurementUnitMapper;

    /**
     * Lista todas as unidades de medida cadastradas no banco de dados.
     * @return lista com todas as unidades de medida encontradas, caso exista.
     */
    public List<MeasurementUnitResponse> readMeasurementUnit() {
        List<MeasurementUnit> measurementUnits = measurementUnitRepository.findAll();
        return measurementUnits.stream()
            .map(measurementUnitMapper::toResponse)
            .toList();
    }
}