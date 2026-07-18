package net.centroweg.gerenciamentocompras.modules.product.service.mapper;

import org.springframework.stereotype.Component;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;

/**
 * Componente responsável pela conversão entre a entidade({@link MeasurementUnit}) e seus DTOs de entrada({@link MeasurementUnitRequest}) e saída({@link MeasurementUnitResponse}).
 */
@Component
public class MeasurementUnitMapper {

    /**
     * Converte um DTO de entrada da unidade de medida em uma entidade unidade de medida.
     * @param request DTO com os dados de entrada da unidade de medida.
     * @return dados convertidos para entidade.
     */
    public MeasurementUnit toEntity(MeasurementUnitRequest request) {
        return new MeasurementUnit(
            request.name(),
            request.abbreviation()
        );
    }

    /**
     * Converte uma entidade unidade de medida em um DTO de saída da unidade de medida.
     * @param measurementUnit entidade com os dados da unidade de medida.
     * @return dados convertidos para DTO de saída.
     */
    public MeasurementUnitResponse toResponse(MeasurementUnit measurementUnit) {
        return new MeasurementUnitResponse(
            measurementUnit.getId(),
            measurementUnit.getName(),
            measurementUnit.getAbbreviation()
        );
    }
}