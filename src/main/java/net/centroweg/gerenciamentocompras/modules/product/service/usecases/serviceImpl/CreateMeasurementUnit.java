package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

@Service
@RequiredArgsConstructor
public class CreateMeasurementUnit {
    
    private final MeasurementUnitRepository measurementUnitRepository;
    private final MeasurementUnitMapper measurementUnitMapper;

    public MeasurementUnitResponse createMeasurementUnit(MeasurementUnitRequest request){
        return measurementUnitMapper.toResponse(measurementUnitRepository.save(measurementUnitMapper.toEntity(request)));
    }

}
