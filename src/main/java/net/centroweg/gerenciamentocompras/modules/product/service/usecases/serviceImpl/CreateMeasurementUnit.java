package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

/**
 * Caso de uso responsável pelo cadastro de unidades de medida.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */

@Service
@RequiredArgsConstructor
public class CreateMeasurementUnit {

    private final MeasurementUnitRepository measurementUnitRepository;
    private final MeasurementUnitMapper measurementUnitMapper;

    /**
     * Cria e persiste uma nova unidade de medida.
     *
     * @param request Dados da unidade de medida a ser cadastrada.
     * @return Dados da unidade de medida cadastrada.
     */
    public MeasurementUnitResponse createMeasurementUnit(MeasurementUnitRequest request){
        return measurementUnitMapper.toResponse(
            measurementUnitRepository.save(
                measurementUnitMapper.toEntity(request)
            )
        );
    }
}