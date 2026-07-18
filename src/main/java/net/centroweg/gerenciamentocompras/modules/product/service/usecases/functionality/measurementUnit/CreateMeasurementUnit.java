package net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.measurementUnit;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;

/**
 * Caso de uso responsável pela criação de uma {@link MeasurementUnit}.
 */
@Service
@RequiredArgsConstructor
public class CreateMeasurementUnit {

    private final MeasurementUnitRepository measurementUnitRepository;
    private final MeasurementUnitMapper measurementUnitMapper;

    /**
     * Cria e persiste uma nova unidade de medida no banco de dados.
     * @param request dados da unidade de medida.
     * @return unidade de medida criada.
     */
    public MeasurementUnitResponse createMeasurementUnit(MeasurementUnitRequest request){
        return measurementUnitMapper.toResponse(
            measurementUnitRepository.save(
                measurementUnitMapper.toEntity(request)
            )
        );
    }
}