package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.auth.filter.SecurityFilter;
import net.centroweg.gerenciamentocompras.modules.request.presentation.controller.ItemRequestProductController;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.ItemRequestProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemRequestProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemRequestProductService itemRequestProductService;

    @MockitoBean
    private SecurityFilter securityFilter;

    private ItemRequestProductRequest validRequest;
    private ItemRequestProductResponse mockResponse;

    @BeforeEach
    void setUp() {
        validRequest = new ItemRequestProductRequest(
                1L,
                "Parafuso",
                "UN",
                10.0,
                "EM_ANDAMENTO",
                "Nenhuma observação adicional"
        );

        mockResponse = new ItemRequestProductResponse(
                1L,
                1L,
                "Parafuso",
                "UN",
                10.0,
                "EM_ANDAMENTO",
                "Nenhuma observação adicional"
        );
    }

    // ─────────────────────────── POST /item-request-products ───────────────────────────

    @Test
    @DisplayName("[Integração] Deve criar item de produto e retornar 201")
    void createItemRequestProduct_shouldReturn201_whenRequestIsValid() throws Exception {
        when(itemRequestProductService.createRequestProduct(any(ItemRequestProductRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/item-request-products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemRequestProduct").value(1L))
                .andExpect(jsonPath("$.requestId").value(1L))
                .andExpect(jsonPath("$.productName").value("Parafuso"))
                .andExpect(jsonPath("$.measurementUnit").value("UN"))
                .andExpect(jsonPath("$.quantity").value(10.0))
                .andExpect(jsonPath("$.statusName").value("EM_ANDAMENTO"))
                .andExpect(jsonPath("$.additionalInformations").value("Nenhuma observação adicional"));

        verify(itemRequestProductService, times(1)).createRequestProduct(any(ItemRequestProductRequest.class));
    }

    @Test
    @DisplayName("[Integração] Deve criar item com additionalInformations em branco")
    void createItemRequestProduct_shouldReturn201_whenAdditionalInformationsIsBlank() throws Exception {
        ItemRequestProductRequest validRequest = new ItemRequestProductRequest(
                1L, "Parafuso", "UN", 10.0, "EM_ANDAMENTO", ""
        );

        mockMvc.perform(post("/item-request-products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated()); // ou isOk(), conforme o que o endpoint retorna

        verify(itemRequestProductService, times(1)).createRequestProduct(any());
    }

    // ─────────────────────────── GET /item-request-products ────────────────────────────

    @Test
    @DisplayName("[Integração] Deve retornar 200 com lista de itens de produto")
    void listItemRequestProduct_shouldReturn200_withFullList() throws Exception {
        ItemRequestProductResponse second = new ItemRequestProductResponse(
                2L, 2L, "Porca", "CX", 5.0, "EM_ANDAMENTO", "Outra observação"
        );

        when(itemRequestProductService.findAllRequestProduct()).thenReturn(List.of(mockResponse, second));

        mockMvc.perform(get("/item-request-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].itemRequestProduct").value(1L))
                .andExpect(jsonPath("$[0].productName").value("Parafuso"))
                .andExpect(jsonPath("$[1].itemRequestProduct").value(2L))
                .andExpect(jsonPath("$[1].productName").value("Porca"));

        verify(itemRequestProductService, times(1)).findAllRequestProduct();
    }

    @Test
    @DisplayName("[Integração] Deve retornar 200 com lista vazia")
    void listItemRequestProduct_shouldReturn200_withEmptyList() throws Exception {
        when(itemRequestProductService.findAllRequestProduct()).thenReturn(List.of());

        mockMvc.perform(get("/item-request-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(itemRequestProductService, times(1)).findAllRequestProduct();
    }

    // ─────────────────────────── GET /item-request-products/{id} ───────────────────────

    @Test
    @DisplayName("[Integração] Deve retornar 200 ao buscar item de produto por ID existente")
    void findByIdItemRequestProduct_shouldReturn200_whenExists() throws Exception {
        when(itemRequestProductService.findRequestProductById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/item-request-products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemRequestProduct").value(1L))
                .andExpect(jsonPath("$.productName").value("Parafuso"))
                .andExpect(jsonPath("$.quantity").value(10.0));

        verify(itemRequestProductService, times(1)).findRequestProductById(1L);
    }

    @Test
    @DisplayName("[Integração] Deve retornar 404 ao buscar item de produto com ID inexistente")
    void findByIdItemRequestProduct_shouldReturn404_whenNotFound() throws Exception {
        when(itemRequestProductService.findRequestProductById(99L))
                .thenThrow(new ItemRequestProductNotFoundException());

        mockMvc.perform(get("/item-request-products/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(itemRequestProductService, times(1)).findRequestProductById(99L);
    }

    // ─────────────────────────── PUT /item-request-products/{id} ───────────────────────

    @Test
    @DisplayName("[Integração] Deve retornar 200 ao atualizar item de produto com sucesso")
    void updateItemRequestProduct_shouldReturn200_whenRequestIsValid() throws Exception {
        ItemRequestProductRequest updateRequest = new ItemRequestProductRequest(
                1L, "Parafuso Atualizado", "KG", 20.0, "CONCLUIDO", "Informações atualizadas"
        );
        ItemRequestProductResponse updatedResponse = new ItemRequestProductResponse(
                1L, 1L, "Parafuso Atualizado", "KG", 20.0, "CONCLUIDO", "Informações atualizadas"
        );

        when(itemRequestProductService.updateRequestProduct(any(ItemRequestProductRequest.class), eq(1L)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/item-request-products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Parafuso Atualizado"))
                .andExpect(jsonPath("$.quantity").value(20.0))
                .andExpect(jsonPath("$.statusName").value("CONCLUIDO"));

        verify(itemRequestProductService, times(1))
                .updateRequestProduct(any(ItemRequestProductRequest.class), eq(1L));
    }

    @Test
    @DisplayName("[Integração] Deve retornar 404 ao atualizar item de produto com ID inexistente")
    void updateItemRequestProduct_shouldReturn404_whenNotFound() throws Exception {
        when(itemRequestProductService.updateRequestProduct(any(ItemRequestProductRequest.class), eq(99L)))
                .thenThrow(new ItemRequestProductNotFoundException());

        mockMvc.perform(put("/item-request-products/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());

        verify(itemRequestProductService, times(1))
                .updateRequestProduct(any(ItemRequestProductRequest.class), eq(99L));
    }

    // ─────────────────────────── DELETE /item-request-products/{id} ────────────────────

    @Test
    @DisplayName("[Integração] Deve retornar 204 ao deletar item de produto com sucesso")
    void deleteItemRequestProduct_shouldReturn204_whenExists() throws Exception {
        doNothing().when(itemRequestProductService).deleteRequestProduct(1L);

        mockMvc.perform(delete("/item-request-products/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(itemRequestProductService, times(1)).deleteRequestProduct(1L);
    }

    @Test
    @DisplayName("[Integração] Deve retornar 404 ao deletar item de produto com ID inexistente")
    void deleteItemRequestProduct_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new ItemRequestProductNotFoundException())
                .when(itemRequestProductService).deleteRequestProduct(99L);

        mockMvc.perform(delete("/item-request-products/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(itemRequestProductService, times(1)).deleteRequestProduct(99L);
    }
}
