package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

@Service
@RequiredArgsConstructor
public class ReadMeasurementUnit {
    
    private final MeasurementUnitRepository measurementUnitRepository;
    private final MeasurementUnitMapper measurementUnitMapper;

    public List<MeasurementUnitResponse> readMeasurementUnit(){
        List<MeasurementUnit> measurementUnits = measurementUnitRepository.findAll();
        return measurementUnits.stream().map(measurementUnitMapper::toResponse).toList();
    }

}
