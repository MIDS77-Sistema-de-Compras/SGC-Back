package net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.measurementUnit;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

/**
 * Caso de uso responsável pela atualização de uma {@link MeasurementUnit}.
 */
@Service
@RequiredArgsConstructor
public class UpdateMeasurementUnit {

    private final MeasurementUnitRepository measurementUnitRepository;
    private final MeasurementUnitMapper measurementUnitMapper;

    /**
     * Atualiza uma unidade de medida existente.
     * @param id identificador da unidade de medida.
     * @param request novos dados da unidade de medida.
     * @return unidade de medida já atualizada.
     * @throws MeasurementUnitNotFoundException caso nenhuma unidade de medida seja encontrada.
     */
    public MeasurementUnitResponse updateMeasurementUnit(Long id, MeasurementUnitRequest request) {
        MeasurementUnit measurementUnit =
            measurementUnitRepository.findById(id)
                .orElseThrow(() -> new MeasurementUnitNotFoundException());
        measurementUnit.setName(request.name());
        measurementUnit.setAbbreviation(request.abbreviation());
        measurementUnitRepository.save(measurementUnit);
        return measurementUnitMapper.toResponse(measurementUnit);
    }
}