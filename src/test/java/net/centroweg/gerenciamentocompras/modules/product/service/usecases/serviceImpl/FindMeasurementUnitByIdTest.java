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
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

@ExtendWith(MockitoExtension.class)
class FindMeasurementUnitByIdTest {

    @Mock
    private MeasurementUnitRepository repository;

    @Mock
    private MeasurementUnitMapper mapper;

    @InjectMocks
    private FindMeasurementUnitById findMeasurementUnitById;

    @Test
    @DisplayName("Deve buscar unidade de medida por ID com sucesso")
    void mustFindMeasurementUnitByIdSuccessfully() {
        Long id = 1L;
        MeasurementUnit entity = new MeasurementUnit(id, "Litros", "L");
        MeasurementUnitResponse expectedResponse = new MeasurementUnitResponse(id, "Litros", "L");

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(expectedResponse);

        MeasurementUnitResponse response = findMeasurementUnitById.findMeasurementUnitById(id);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("Litros", response.name());
        assertEquals("L", response.abbreviation());

        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar MeasurementUnitNotFoundException quando ID não existir")
    void mustThrowMeasurementUnitNotFoundExceptionWhenIdDoesNotExist() {
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MeasurementUnitNotFoundException.class, () -> {
            findMeasurementUnitById.findMeasurementUnitById(id);
        });

        verify(repository, times(1)).findById(id);
        verify(mapper, never()).toResponse(any());
    }
}
