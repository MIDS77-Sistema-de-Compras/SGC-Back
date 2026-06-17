package net.centroweg.gerenciamentocompras.modules.request.service;

import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status.StatusServiceImpl;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status.AddStatusService;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status.FindStatusByIdService;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status.DeleteStatusService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock
    private AddStatusService addStatusService;
    @Mock
    private FindStatusByIdService findStatusByIdService;
    @Mock
    private DeleteStatusService deleteStatusService;

    @InjectMocks
    private StatusServiceImpl statusService;

    @Test
    @DisplayName("RN-STA09 — Deve lançar exceção ao tentar criar um status com nome já existente")
    void shouldThrowExceptionWhenStatusIsDuplicate() {
        String duplicateName = "Aprovado";
        StatusRequest duplicateRequest = new StatusRequest(duplicateName, "Descrição válida com mais de dez caracteres");

        when(addStatusService.addStatus(duplicateRequest)).thenThrow(new StatusAlreadyExistsException());

        assertThrows(StatusAlreadyExistsException.class, () -> {
            statusService.createStatus(duplicateRequest);
        });

        verify(addStatusService, times(1)).addStatus(duplicateRequest);
    }

    @Test
    @DisplayName("Deve criar um status com sucesso quando não for duplicado")
    void shouldCreateStatusSuccessfully() {
        StatusRequest validRequest = new StatusRequest("Novo Status", "Descrição com mais de dez caracteres");
        StatusResponse fakeResponse = new StatusResponse(1L, "Novo Status", "Descrição com mais de dez caracteres");

        when(addStatusService.addStatus(validRequest)).thenReturn(fakeResponse);

        StatusResponse result = statusService.createStatus(validRequest);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Novo Status", result.name());
    }

    @Test
    @DisplayName("Deve buscar um status por ID com sucesso")
    void shouldFindStatusById() {
        StatusResponse fakeResponse = new StatusResponse(1L, "Entregue", "Processo concluído com sucesso");

        when(findStatusByIdService.findStatusById(1L)).thenReturn(fakeResponse);

        StatusResponse result = statusService.findStatusById(1L);

        assertNotNull(result);
        assertEquals("Entregue", result.name());
    }

    @Test
    @DisplayName("Deve deletar um status por ID com sucesso")
    void shouldDeleteStatus() {
        Long idToDelete = 1L;

        assertDoesNotThrow(() -> statusService.deleteStatus(idToDelete));

        verify(deleteStatusService, times(1)).deleteStatus(idToDelete);
    }
}