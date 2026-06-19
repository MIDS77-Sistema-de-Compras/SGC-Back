package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

/**
 * Caso de uso responsável pela atualização de unidades de medida.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class UpdateMeasurementUnit {

    private final MeasurementUnitRepository measurementUnitRepository;
    private final MeasurementUnitMapper measurementUnitMapper;

    /**
     * Atualiza os dados de uma unidade de medida existente.
     *
     * @param id Identificador único da unidade de medida.
     * @param request Novos dados da unidade de medida.
     * @return Dados atualizados da unidade de medida.
     * @throws MeasurementUnitNotFoundException Caso nenhuma unidade
     * seja encontrada com o ID informado.
     */
    public MeasurementUnitResponse updateMeasurementUnit(
            Long id,
            MeasurementUnitRequest request) {

        MeasurementUnit measurementUnit =
            measurementUnitRepository.findById(id)
                .orElseThrow(() -> new MeasurementUnitNotFoundException());

        measurementUnit.setName(request.name());
        measurementUnit.setAbbreviation(request.abbreviation());

        measurementUnitRepository.save(measurementUnit);

        return measurementUnitMapper.toResponse(measurementUnit);
    }
}