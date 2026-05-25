package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
class FindMeasurementUnitByAbbreviationTest {

    @Mock
    private MeasurementUnitRepository repository;

    @Mock
    private MeasurementUnitMapper mapper;

    @InjectMocks
    private FindMeasurementUnitByAbbreviation findByAbbreviation;

    @Test
    @DisplayName("Deve retornar a unidade de medida quando buscar por abreviação existente")
    void mustReturnUnitWhenSearchByAbbreviation() {

        String abbreviation = "KG";
        MeasurementUnit entity = new MeasurementUnit(1L, "Quilograma", "KG");
        MeasurementUnitResponse expectedResponse = new MeasurementUnitResponse(1L, "Quilograma", "KG");

        when(repository.findByAbbreviation(abbreviation)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(expectedResponse);

        MeasurementUnitResponse response = findByAbbreviation.findMeasurementUnitByAbbreviation(abbreviation);

        assertNotNull(response);
        assertEquals("KG", response.abbreviation());
    }

    @Test
    @DisplayName("Deve lancar MeasurementUnitNotFoundException quando abreviação não existir")
    void MustThrowErrorWhenAbbreviationDoesNotExist() {

        String abbreviation = "XYZ";
        when(repository.findByAbbreviation(abbreviation)).thenReturn(Optional.empty());

        assertThrows(MeasurementUnitNotFoundException.class, () -> {
            findByAbbreviation.findMeasurementUnitByAbbreviation(abbreviation);
        });

        verify(mapper, never()).toResponse(any());
    }
}