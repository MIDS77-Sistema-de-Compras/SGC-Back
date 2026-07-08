package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestStatus;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.RequestService;
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
 * Testes unitários (Mockito, sem contexto Spring) do {@link RequestController}.
 *
 * <p>Complementam o teste de integração {@code RequestControllerTest} (que sobe o Spring e o
 * {@code AuditLogAspect}). Aqui isolamos o contrato do controller: delegação e status HTTP,
 * garantindo que o ID do path (anotado com {@code @AuditParam("request")}) é repassado ao serviço.</p>
 *
 * <p>Os corpos de request são passados como {@code null} de propósito: o controller apenas os
 * repassa ao serviço, sem inspecioná-los, então o valor é irrelevante para o contrato de delegação
 * (e records finais não são adequados para mock).</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RequestController - testes unitários")
class RequestControllerUnitTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController controller;

    private RequestResponse sampleResponse(Long id) {
        return new RequestResponse(id, null, null, 100L, "EM_ANDAMENTO", null, "Fulano", "1234", null, null, null);
    }

    @Test
    @DisplayName("POST createRequest deve delegar corpo e principal e retornar 201 Created")
    void deveCriarSolicitacao() {
        UserPrincipal principal = mock(UserPrincipal.class);
        RequestRequest body = null;
        RequestResponse response = sampleResponse(1L);
        when(requestService.createRequest(body, principal)).thenReturn(response);

        ResponseEntity<RequestResponse> result = controller.createRequest(body, principal);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
        verify(requestService, times(1)).createRequest(body, principal);
    }

    @Test
    @DisplayName("PUT updateRequest deve repassar o id do path (@AuditParam request) e retornar 200 OK")
    void deveAtualizarSolicitacao() {
        UpdateRequestRequest body = null;
        RequestResponse response = sampleResponse(5L);
        when(requestService.updateRequest(body, 5L)).thenReturn(response);

        ResponseEntity<RequestResponse> result = controller.updateRequest(body, 5L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(requestService, times(1)).updateRequest(body, 5L);
    }

    @Test
    @DisplayName("DELETE deleteRequest deve repassar o id do path e retornar 204 No Content")
    void deveDesativarSolicitacao() {
        ResponseEntity<Void> result = controller.deleteRequest(9L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(requestService, times(1)).deleteRequest(9L);
    }

    @Test
    @DisplayName("PATCH updateFeedback deve repassar o id do path e retornar 200 OK")
    void deveAdicionarFeedback() {
        UpdateFeedback body = null;
        RequestResponse response = sampleResponse(3L);
        when(requestService.updateFeedback(body, 3L)).thenReturn(response);

        ResponseEntity<RequestResponse> result = controller.updateFeedback(body, 3L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(requestService, times(1)).updateFeedback(body, 3L);
    }

    @Test
    @DisplayName("PATCH updateStatus deve repassar o id do path e retornar 200 OK")
    void deveAtualizarStatus() {
        UpdateRequestStatus body = null;
        RequestResponse response = sampleResponse(4L);
        when(requestService.updateStatus(4L, body)).thenReturn(response);

        ResponseEntity<RequestResponse> result = controller.updateStatus(4L, body);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(requestService, times(1)).updateStatus(4L, body);
    }
}
