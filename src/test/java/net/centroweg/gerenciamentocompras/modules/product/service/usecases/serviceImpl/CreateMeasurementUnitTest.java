package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
class CreateMeasurementUnitTest {

    @Mock
    private MeasurementUnitRepository repository;

    @Mock
    private MeasurementUnitMapper mapper;

    @InjectMocks
    private CreateMeasurementUnit createMeasurementUnit;

    @Test
    @DisplayName("Deve cadastrar uma unidade de medida com sucesso")
    void mustRegisterUnitSuccessfully() {

        MeasurementUnitRequest request = new MeasurementUnitRequest("Quilograma", "KG");
        MeasurementUnit entity = new MeasurementUnit("Quilograma", "KG");
        MeasurementUnit savedEntity = new MeasurementUnit(1L, "Quilograma", "KG");
        MeasurementUnitResponse expectedResponse = new MeasurementUnitResponse(1L, "Quilograma", "KG");

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        MeasurementUnitResponse response = createMeasurementUnit.createMeasurementUnit(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Quilograma", response.name());
        assertEquals("KG", response.abbreviation());
        
        verify(repository, times(1)).save(entity);
    }
}