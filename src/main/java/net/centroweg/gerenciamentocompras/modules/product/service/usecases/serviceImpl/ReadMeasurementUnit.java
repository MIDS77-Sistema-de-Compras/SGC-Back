package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

/**
 * Caso de uso responsável pela listagem de unidades de medida.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class ReadMeasurementUnit {

    private final MeasurementUnitRepository measurementUnitRepository;
    private final MeasurementUnitMapper measurementUnitMapper;

    /**
     * Recupera todas as unidades de medida cadastradas.
     *
     * @return Lista contendo todas as unidades de medida encontradas.
     */
    public List<MeasurementUnitResponse> readMeasurementUnit() {

        List<MeasurementUnit> measurementUnits =
            measurementUnitRepository.findAll();

        return measurementUnits.stream()
            .map(measurementUnitMapper::toResponse)
            .toList();
    }
}