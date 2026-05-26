package net.centroweg.gerenciamentocompras.modules.request.service;

import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusAlreadyExistsException; // IMPORT CORRIGIDO
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.StatusServiceImpl;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.AddStatusService;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.FindStatusByIdService;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.DeleteStatusService;

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
    void deveLancarExcecaoQuandoStatusForDuplicado() {
        String nomeAprovado = StatusName.APROVADO.getValue();
        StatusRequest requestDuplicado = new StatusRequest(nomeAprovado, "Descrição válida com mais de dez caracteres");

        when(addStatusService.addStatus(requestDuplicado)).thenThrow(new StatusAlreadyExistsException());

        assertThrows(StatusAlreadyExistsException.class, () -> {
            statusService.createStatus(requestDuplicado);
        });
    }

    @Test
    @DisplayName("Deve criar um status com sucesso quando não for duplicado")
    void deveCriarStatusComSucesso() {
        StatusRequest requestValido = new StatusRequest("Novo Status", "Descrição com mais de dez caracteres");
        StatusResponse responseFake = new StatusResponse(1L, "Novo Status", "Descrição com mais de dez caracteres");

        when(addStatusService.addStatus(requestValido)).thenReturn(responseFake);

        StatusResponse resultado = statusService.createStatus(requestValido);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Novo Status", resultado.name());
    }

    @Test
    @DisplayName("Deve buscar um status por ID com sucesso")
    void deveBuscarStatusPorId() {
        StatusResponse responseFake = new StatusResponse(1L, "Entregue", "Processo concluído com sucesso");

        when(findStatusByIdService.findStatusById(1L)).thenReturn(responseFake);

        StatusResponse resultado = statusService.findStatusById(1L);

        assertNotNull(resultado);
        assertEquals("Entregue", resultado.name());
    }

    @Test
    @DisplayName("Deve deletar um status por ID com sucesso")
    void deveDeletarStatus() {
        Long idParaDeletar = 1L;

        assertDoesNotThrow(() -> statusService.deleteStatus(idParaDeletar));

        verify(deleteStatusService, times(1)).deleteStatus(idParaDeletar);
    }
}