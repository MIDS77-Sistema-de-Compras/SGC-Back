package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf;

import java.util.List;

import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;

public interface MeasurementUnitService {
    MeasurementUnitResponse createMeasurementUnit (MeasurementUnitRequest request);
    List<MeasurementUnitResponse> readMeasurementUnit();
    MeasurementUnitResponse updateMeasurementUnit (Long id, MeasurementUnitRequest request);
    MeasurementUnitResponse findMeasurementUnitById(Long id);
    MeasurementUnitResponse findMeasurementUnitByAbbreviation (String abbreviation);
}
