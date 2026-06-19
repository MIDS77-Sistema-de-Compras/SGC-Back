package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

/**
 * Caso de uso responsável pela busca de unidades de medida
 * com base no identificador único.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class FindMeasurementUnitById {

    private final MeasurementUnitMapper measurementUnitMapper;
    private final MeasurementUnitRepository measurementUnitRepository;

    /**
     * Busca uma unidade de medida pelo ID.
     *
     * @param id Identificador único da unidade de medida.
     * @return Dados da unidade de medida encontrada.
     * @throws MeasurementUnitNotFoundException Caso nenhuma unidade
     * seja encontrada com o ID informado.
     */
    public MeasurementUnitResponse findMeasurementUnitById(Long id) {

        return measurementUnitMapper.toResponse(
            measurementUnitRepository.findById(id)
                .orElseThrow(() -> new MeasurementUnitNotFoundException())
        );
    }
}