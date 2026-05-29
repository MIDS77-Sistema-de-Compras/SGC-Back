package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.MeasurementUnitMapper;

@ExtendWith(MockitoExtension.class)
class ReadMeasurementUnitTest {

    @Mock
    private MeasurementUnitRepository repository;

    @Mock
    private MeasurementUnitMapper mapper;

    @InjectMocks
    private ReadMeasurementUnit readMeasurementUnit;

    @Test
    @DisplayName("Deve listar todas as unidades de medida com sucesso")
    void mustReadAllMeasurementUnitsSuccessfully() {
        MeasurementUnit u1 = new MeasurementUnit(1L, "Litros", "L");
        MeasurementUnit u2 = new MeasurementUnit(2L, "Metros", "M");
        List<MeasurementUnit> entities = List.of(u1, u2);

        MeasurementUnitResponse r1 = new MeasurementUnitResponse(1L, "Litros", "L");
        MeasurementUnitResponse r2 = new MeasurementUnitResponse(2L, "Metros", "M");

        when(repository.findAll()).thenReturn(entities);
        when(mapper.toResponse(u1)).thenReturn(r1);
        when(mapper.toResponse(u2)).thenReturn(r2);

        List<MeasurementUnitResponse> response = readMeasurementUnit.readMeasurementUnit();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Litros", response.get(0).name());
        assertEquals("Metros", response.get(1).name());

        verify(repository, times(1)).findAll();
    }
}
