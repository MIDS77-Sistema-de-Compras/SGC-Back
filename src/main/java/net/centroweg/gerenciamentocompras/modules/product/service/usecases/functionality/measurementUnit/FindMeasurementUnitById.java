package net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.measurementUnit;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;

/**
 * Caso de uso responsável pela busca de uma {@link MeasurementUnit} pelo seu identificador.
 */
@Service
@RequiredArgsConstructor
public class FindMeasurementUnitById {

    private final MeasurementUnitMapper measurementUnitMapper;
    private final MeasurementUnitRepository measurementUnitRepository;

    /**
     * Busca uma unidade de medida no banco de dados pelo ID informado.
     * @param id identificador da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     * @throws MeasurementUnitNotFoundException caso nenhuma unidade de medida seja encontrada.
     */
    public MeasurementUnitResponse findMeasurementUnitById(Long id) {
        return measurementUnitMapper.toResponse(
            measurementUnitRepository.findById(id)
                .orElseThrow(() -> new MeasurementUnitNotFoundException())
        );
    }
}