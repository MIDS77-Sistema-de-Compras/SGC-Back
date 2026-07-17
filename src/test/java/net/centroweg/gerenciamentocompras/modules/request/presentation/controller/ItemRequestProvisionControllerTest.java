package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceIntrf.ItemRequestProvisionService;
import net.centroweg.gerenciamentocompras.shared.security.annotation.CanManagePurchaseItems;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Testes unitários (Mockito) do {@link ItemRequestProvisionController}.
 *
 * <p>Validam a delegação ao serviço e os status HTTP. Ver {@code AuditAnnotationsTest} para o
 * achado de que apenas o POST possui {@code @Auditable} (sem {@code @AuditParam}), enquanto PUT e
 * DELETE não são auditados hoje.</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ItemRequestProvisionController - testes unitários")
class ItemRequestProvisionControllerTest {

    @Mock
    private ItemRequestProvisionService service;

    @InjectMocks
    private ItemRequestProvisionController controller;

    private ItemRequestProvisionRequest sampleRequest() {
        return new ItemRequestProvisionRequest(10L, 20L, 30L, "obs");
    }

    private ItemRequestProvisionResponse sampleResponse() {
        return new ItemRequestProvisionResponse(1L, 10L, 20L, "EM_ANDAMENTO", "obs");
    }

    @Test
    @DisplayName("POST addItem deve delegar ao serviço e retornar 201 Created")
    void deveAdicionarItem() {
        ItemRequestProvisionRequest request = sampleRequest();
        ItemRequestProvisionResponse response = sampleResponse();
        when(service.addItemToProvisionRequest(request)).thenReturn(response);

        ResponseEntity<ItemRequestProvisionResponse> result = controller.addItem(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
        assertThat(result.getBody().requestId()).isEqualTo(10L);
        verify(service, times(1)).addItemToProvisionRequest(request);
    }

    @Test
    @DisplayName("PUT updateItem deve delegar id e corpo ao serviço e retornar 200 OK")
    void deveAtualizarItem() {
        ItemRequestProvisionRequest request = sampleRequest();
        ItemRequestProvisionResponse response = sampleResponse();
        when(service.updateItemFromProvisionRequest(1L, request)).thenReturn(response);

        ResponseEntity<ItemRequestProvisionResponse> result = controller.updateItem(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(service, times(1)).updateItemFromProvisionRequest(1L, request);
    }

    @Test
    @DisplayName("DELETE deleteItem deve delegar id ao serviço e retornar 204 No Content")
    void deveRemoverItem() {
        ResponseEntity<Void> result = controller.deleteItem(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(service, times(1)).deleteItemFromProvisionRequest(1L);
    }

    @Test
    @DisplayName("Deve proteger somente o PUT de atualizacao")
    void deveProtegerSomenteAtualizacao() throws NoSuchMethodException {
        Method updateItem = ItemRequestProvisionController.class.getMethod(
                "updateItem", Long.class, ItemRequestProvisionRequest.class
        );
        Method addItem = ItemRequestProvisionController.class.getMethod(
                "addItem", ItemRequestProvisionRequest.class
        );
        Method findAllItems = ItemRequestProvisionController.class.getMethod("findAllItems", Long.class);
        Method deleteItem = ItemRequestProvisionController.class.getMethod("deleteItem", Long.class);

        assertThat(ItemRequestProvisionController.class.isAnnotationPresent(CanManagePurchaseItems.class)).isFalse();
        assertThat(updateItem.isAnnotationPresent(CanManagePurchaseItems.class)).isTrue();
        assertThat(addItem.isAnnotationPresent(CanManagePurchaseItems.class)).isTrue();
        assertThat(findAllItems.isAnnotationPresent(CanManagePurchaseItems.class)).isFalse();
        assertThat(deleteItem.isAnnotationPresent(CanManagePurchaseItems.class)).isTrue();
    }
}
