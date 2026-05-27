package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

@Service
@RequiredArgsConstructor
public class FindMeasurementUnitByAbbreviation {
    
    private final MeasurementUnitMapper measurementUnitMapper;
    private final MeasurementUnitRepository measurementUnitRepository;

    public MeasurementUnitResponse findMeasurementUnitByAbbreviation (String abbreviation){
        return measurementUnitMapper.toResponse(
        measurementUnitRepository.findByAbbreviation(abbreviation)
            .orElseThrow(() -> new MeasurementUnitNotFoundException()));
    }

}
