package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceIntrf.ItemRequestProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Testes unitários (Mockito) do {@link ItemRequestProductController}.
 *
 * <p>Validam a delegação ao serviço e os status HTTP. A verificação das anotações de auditoria
 * (e do fato de que o {@code @AuditParam("request")} está no corpo do DTO, e não em um {@code Long},
 * portanto o aspecto não consegue resolver o vínculo com a solicitação-pai) está documentada em
 * {@code AuditAnnotationsTest}.</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ItemRequestProductController - testes unitários")
class ItemRequestProductControllerTest {

    @Mock
    private ItemRequestProductService service;

    @InjectMocks
    private ItemRequestProductController controller;

    private ItemRequestProductRequest sampleRequest() {
        return new ItemRequestProductRequest(10L, "Cimento", null, "SACO", 5.0, "EM_ANDAMENTO", "obs");
    }

    private ItemRequestProductResponse sampleResponse() {
        return new ItemRequestProductResponse(1L, 10L, "Cimento", null, "SACO", 5.0, "EM_ANDAMENTO", "obs");
    }

    @Test
    @DisplayName("POST deve delegar ao serviço e retornar 201 Created")
    void deveCriarItem() {
        ItemRequestProductRequest request = sampleRequest();
        ItemRequestProductResponse response = sampleResponse();
        when(service.createRequestProduct(request)).thenReturn(response);

        ResponseEntity<ItemRequestProductResponse> result = controller.createItemRequestProduct(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
        // O requestId trafega DENTRO do DTO; confirmamos que ele é repassado íntegro ao serviço.
        assertThat(result.getBody().requestId()).isEqualTo(10L);
        verify(service, times(1)).createRequestProduct(request);
    }

    @Test
    @DisplayName("PUT deve delegar id do path e corpo ao serviço e retornar 200 OK")
    void deveAtualizarItem() {
        ItemRequestProductRequest request = sampleRequest();
        ItemRequestProductResponse response = sampleResponse();
        when(service.updateRequestProduct(request, 1L)).thenReturn(response);

        ResponseEntity<ItemRequestProductResponse> result = controller.updateItemRequestProduct(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(service, times(1)).updateRequestProduct(request, 1L);
    }

    @Test
    @DisplayName("DELETE deve delegar id ao serviço e retornar 204 No Content")
    void deveRemoverItem() {
        ResponseEntity<Void> result = controller.deleteItemRequestProduct(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(service, times(1)).deleteRequestProduct(1L);
    }
}
