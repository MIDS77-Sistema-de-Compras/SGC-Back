package net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.measurementUnit;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;

/**
 * Caso de uso responsável por buscar uma {@link MeasurementUnit} pela sua abreviação.
 */
@Service
@RequiredArgsConstructor
public class FindMeasurementUnitByAbbreviation {

    private final MeasurementUnitMapper measurementUnitMapper;
    private final MeasurementUnitRepository measurementUnitRepository;

    /**
     * Busca uma unidade de medida no banco de dados pela abreviação informada.
     * @param abbreviation abreviação(sigla) da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     * @throws MeasurementUnitNotFoundException caso a unidade de medida não seja encontrada.
     */
    public MeasurementUnitResponse findMeasurementUnitByAbbreviation(String abbreviation) {
        return measurementUnitMapper.toResponse(measurementUnitRepository.findByAbbreviation(abbreviation)
                .orElseThrow(() -> new MeasurementUnitNotFoundException())
        );
    }
}