package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

/**
 * Caso de uso responsável pela busca de unidades de medida
 * com base em sua abreviação.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class FindMeasurementUnitByAbbreviation {

    private final MeasurementUnitMapper measurementUnitMapper;
    private final MeasurementUnitRepository measurementUnitRepository;

    /**
     * Busca uma unidade de medida pela abreviação.
     *
     * @param abbreviation Sigla da unidade de medida
     * (ex.: "KG", "UN").
     * @return Dados da unidade de medida encontrada.
     * @throws MeasurementUnitNotFoundException Caso nenhuma unidade
     * seja encontrada com a abreviação informada.
     */
    public MeasurementUnitResponse findMeasurementUnitByAbbreviation(String abbreviation) {

        return measurementUnitMapper.toResponse(
            measurementUnitRepository.findByAbbreviation(abbreviation)
                .orElseThrow(() -> new MeasurementUnitNotFoundException())
        );
    }
}