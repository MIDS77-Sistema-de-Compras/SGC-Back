package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

@ExtendWith(MockitoExtension.class)
class UpdateMeasurementUnitTest {

    @Mock
    private MeasurementUnitRepository repository;

    @Mock
    private MeasurementUnitMapper mapper;

    @InjectMocks
    private UpdateMeasurementUnit updateMeasurementUnit;

    @Test
    @DisplayName("Deve atualizar os dados de uma unidade de medida existente com sucesso")
    void mustUpdateSucess() {

        Long id = 1L;
        MeasurementUnitRequest request = new MeasurementUnitRequest("Litros Alterado", "L");
        MeasurementUnit entityExistente = new MeasurementUnit(id, "Litros", "LT");
        MeasurementUnitResponse expectedResponse = new MeasurementUnitResponse(id, "Litros Alterado", "L");

        when(repository.findById(id)).thenReturn(Optional.of(entityExistente));
        when(mapper.toResponse(entityExistente)).thenReturn(expectedResponse);

        MeasurementUnitResponse response = updateMeasurementUnit.updateMeasurementUnit(id, request);

        assertNotNull(response);
        assertEquals("Litros Alterado", entityExistente.getName());
        assertEquals("L", entityExistente.getAbbreviation());
        
        verify(repository, times(1)).save(entityExistente);
    }
}