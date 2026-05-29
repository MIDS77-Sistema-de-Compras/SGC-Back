package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

@Service
@RequiredArgsConstructor
public class FindMeasurementUnitById {
    
    private final MeasurementUnitMapper measurementUnitMapper;
    private final MeasurementUnitRepository measurementUnitRepository;

    public MeasurementUnitResponse findMeasurementUnitById(Long id) {
    return measurementUnitMapper.toResponse(
        measurementUnitRepository.findById(id)
            .orElseThrow(() -> new MeasurementUnitNotFoundException())
    );
}


}
