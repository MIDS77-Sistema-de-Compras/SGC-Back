package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

@Service
@RequiredArgsConstructor
public class UpdateMeasurementUnit {
    
    private final MeasurementUnitRepository measurementUnitRepository;
    private final MeasurementUnitMapper measurementUnitMapper;

    public MeasurementUnitResponse updateMeasurementUnit (Long id, MeasurementUnitRequest request){

       MeasurementUnit measurementUnit = measurementUnitRepository.findById(id)
            .orElseThrow(() -> new MeasurementUnitNotFoundException());

        measurementUnit.setName(request.name());
        measurementUnit.setAbbreviation(request.abbreviation());

        measurementUnitRepository.save(measurementUnit);

        return measurementUnitMapper.toResponse(measurementUnit);
        
    }
}
