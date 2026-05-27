package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.MeasurementUnitService;

@RequiredArgsConstructor
@Service
public class MeasurementUnitServiceImpl implements MeasurementUnitService {
    private final CreateMeasurementUnit create;
    private final ReadMeasurementUnit read;
    private final UpdateMeasurementUnit update;
    private final FindMeasurementUnitById findById;
    private final FindMeasurementUnitByAbbreviation findByAbbreviation;
    
    @Override
    public MeasurementUnitResponse createMeasurementUnit(MeasurementUnitRequest request) {
        return create.createMeasurementUnit(request);
    }

    @Override
    public List<MeasurementUnitResponse> readMeasurementUnit() {
      return read.readMeasurementUnit(); 
    }

    @Override
    public MeasurementUnitResponse updateMeasurementUnit(Long id, MeasurementUnitRequest request) {
       return update.updateMeasurementUnit(id, request);
    }

    @Override
    public MeasurementUnitResponse findMeasurementUnitById(Long id) {
       return findById.findMeasurementUnitById(id);
    }

     @Override
      public MeasurementUnitResponse findMeasurementUnitByAbbreviation(String abbreviation) {
       return findByAbbreviation.findMeasurementUnitByAbbreviation(abbreviation);
      }


    
}
