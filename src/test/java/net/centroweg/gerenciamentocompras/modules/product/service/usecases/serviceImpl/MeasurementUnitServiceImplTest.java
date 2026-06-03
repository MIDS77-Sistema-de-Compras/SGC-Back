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

import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;

@ExtendWith(MockitoExtension.class)
class MeasurementUnitServiceImplTest {

    @Mock
    private CreateMeasurementUnit create;

    @Mock
    private ReadMeasurementUnit read;

    @Mock
    private UpdateMeasurementUnit update;

    @Mock
    private FindMeasurementUnitById findById;

    @Mock
    private FindMeasurementUnitByAbbreviation findByAbbreviation;

    @InjectMocks
    private MeasurementUnitServiceImpl service;

    @Test
    @DisplayName("Deve delegar chamada de criação para o CreateMeasurementUnit")
    void mustDelegateCreateSuccessfully() {
        MeasurementUnitRequest request = new MeasurementUnitRequest("Litros", "L");
        MeasurementUnitResponse expected = new MeasurementUnitResponse(1L, "Litros", "L");

        when(create.createMeasurementUnit(request)).thenReturn(expected);

        MeasurementUnitResponse response = service.createMeasurementUnit(request);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(create, times(1)).createMeasurementUnit(request);
    }

    @Test
    @DisplayName("Deve delegar chamada de listagem para o ReadMeasurementUnit")
    void mustDelegateReadSuccessfully() {
        List<MeasurementUnitResponse> expected = List.of(new MeasurementUnitResponse(1L, "Litros", "L"));

        when(read.readMeasurementUnit()).thenReturn(expected);

        List<MeasurementUnitResponse> response = service.readMeasurementUnit();

        assertNotNull(response);
        assertEquals(expected, response);
        verify(read, times(1)).readMeasurementUnit();
    }

    @Test
    @DisplayName("Deve delegar chamada de atualização para o UpdateMeasurementUnit")
    void mustDelegateUpdateSuccessfully() {
        Long id = 1L;
        MeasurementUnitRequest request = new MeasurementUnitRequest("Litros", "L");
        MeasurementUnitResponse expected = new MeasurementUnitResponse(id, "Litros", "L");

        when(update.updateMeasurementUnit(id, request)).thenReturn(expected);

        MeasurementUnitResponse response = service.updateMeasurementUnit(id, request);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(update, times(1)).updateMeasurementUnit(id, request);
    }

    @Test
    @DisplayName("Deve delegar chamada de busca por ID para o FindMeasurementUnitById")
    void mustDelegateFindByIdSuccessfully() {
        Long id = 1L;
        MeasurementUnitResponse expected = new MeasurementUnitResponse(id, "Litros", "L");

        when(findById.findMeasurementUnitById(id)).thenReturn(expected);

        MeasurementUnitResponse response = service.findMeasurementUnitById(id);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(findById, times(1)).findMeasurementUnitById(id);
    }

    @Test
    @DisplayName("Deve delegar chamada de busca por abreviação para o FindMeasurementUnitByAbbreviation")
    void mustDelegateFindByAbbreviationSuccessfully() {
        String abb = "L";
        MeasurementUnitResponse expected = new MeasurementUnitResponse(1L, "Litros", "L");

        when(findByAbbreviation.findMeasurementUnitByAbbreviation(abb)).thenReturn(expected);

        MeasurementUnitResponse response = service.findMeasurementUnitByAbbreviation(abb);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(findByAbbreviation, times(1)).findMeasurementUnitByAbbreviation(abb);
    }
}
