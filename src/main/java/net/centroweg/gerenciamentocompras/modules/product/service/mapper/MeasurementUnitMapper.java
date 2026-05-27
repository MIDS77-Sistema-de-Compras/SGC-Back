package net.centroweg.gerenciamentocompras.modules.product.service.mapper;

import org.springframework.stereotype.Component;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;

@Component
public class MeasurementUnitMapper {
   
    public MeasurementUnit toEntity(MeasurementUnitRequest request){
        return new MeasurementUnit(
            request.name(),
            request.abbreviation()
        );
    }

    public MeasurementUnitResponse toResponse(MeasurementUnit measurementUnit){
        return new MeasurementUnitResponse(
            measurementUnit.getId(),
            measurementUnit.getName(),
            measurementUnit.getAbbreviation()                                                                                                                           
        );
    }

}
