package net.centroweg.gerenciamentocompras.modules.request.service;

import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct.CreateRequestProductService;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct.DeleteItemRequestProductService;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct.FindAllItemRequestProductService;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct.FindByIdItemRequestProductService;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct.ItemRequestProductServiceImpl;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct.UpdateItemRequestProductService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestProductServiceTest {

    @Mock
    private CreateRequestProductService createRequestProductService;
    @Mock
    private FindAllItemRequestProductService findAllRequestProductService;
    @Mock
    private FindByIdItemRequestProductService findRequestProductByIdService;
    @Mock
    private UpdateItemRequestProductService updateRequestProductService;
    @Mock
    private DeleteItemRequestProductService deleteRequestProductService;

    @InjectMocks
    private ItemRequestProductServiceImpl itemRequestProductService;

    // ───────────────────────────── CREATE ─────────────────────────────

    @Test
    @DisplayName("Deve criar um item de produto com sucesso")
    void shouldCreateItemRequestProductSuccessfully() {
        ItemRequestProductRequest request = new ItemRequestProductRequest(
                1L, "Parafuso", "UN", 10.0, "EM_ANDAMENTO", "Nenhuma observação adicional"
        );
        ItemRequestProductResponse fakeResponse = new ItemRequestProductResponse(
                1L, 1L, "Parafuso", "UN", 10.0, "EM_ANDAMENTO", "Nenhuma observação adicional"
        );

        when(createRequestProductService.create(request)).thenReturn(fakeResponse);

        ItemRequestProductResponse result = itemRequestProductService.createRequestProduct(request);

        assertNotNull(result);
        assertEquals(1L, result.itemRequestProduct());
        assertEquals(1L, result.requestId());
        assertEquals("Parafuso", result.productName());
        assertEquals(10.0, result.quantity());
        verify(createRequestProductService, times(1)).create(request);
    }

    // ───────────────────────────── FIND ALL ───────────────────────────

    @Test
    @DisplayName("Deve retornar a lista de todos os itens de produto")
    void shouldFindAllItemRequestProducts() {
        ItemRequestProductResponse item1 = new ItemRequestProductResponse(
                1L, 1L, "Parafuso", "UN", 10.0, "EM_ANDAMENTO", "Obs 1"
        );
        ItemRequestProductResponse item2 = new ItemRequestProductResponse(
                2L, 2L, "Porca", "CX", 5.0, "EM_ANDAMENTO", "Obs 2"
        );

        when(findAllRequestProductService.findAll()).thenReturn(List.of(item1, item2));

        List<ItemRequestProductResponse> result = itemRequestProductService.findAllRequestProduct();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Parafuso", result.get(0).productName());
        assertEquals("Porca", result.get(1).productName());
        verify(findAllRequestProductService, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver itens de produto")
    void shouldReturnEmptyListWhenNoItemRequestProducts() {
        when(findAllRequestProductService.findAll()).thenReturn(List.of());

        List<ItemRequestProductResponse> result = itemRequestProductService.findAllRequestProduct();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(findAllRequestProductService, times(1)).findAll();
    }

    // ───────────────────────────── FIND BY ID ─────────────────────────

    @Test
    @DisplayName("Deve buscar um item de produto por ID com sucesso")
    void shouldFindItemRequestProductById() {
        ItemRequestProductResponse fakeResponse = new ItemRequestProductResponse(
                1L, 1L, "Parafuso", "UN", 10.0, "EM_ANDAMENTO", "Nenhuma observação adicional"
        );

        when(findRequestProductByIdService.findById(1L)).thenReturn(fakeResponse);

        ItemRequestProductResponse result = itemRequestProductService.findRequestProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.itemRequestProduct());
        assertEquals(1L, result.requestId());
        assertEquals("Parafuso", result.productName());
        verify(findRequestProductByIdService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar item de produto com ID inexistente")
    void shouldThrowExceptionWhenFindingItemRequestProductByNonExistentId() {
        when(findRequestProductByIdService.findById(99L))
                .thenThrow(new ItemRequestProductNotFoundException());

        assertThrows(ItemRequestProductNotFoundException.class, () ->
                itemRequestProductService.findRequestProductById(99L)
        );

        verify(findRequestProductByIdService, times(1)).findById(99L);
    }

    // ───────────────────────────── UPDATE ─────────────────────────────

    @Test
    @DisplayName("Deve atualizar um item de produto com sucesso")
    void shouldUpdateItemRequestProductSuccessfully() {
        ItemRequestProductRequest updateRequest = new ItemRequestProductRequest(
                1L, "Parafuso Atualizado", "KG", 20.0, "CONCLUIDO", "Informações atualizadas"
        );
        ItemRequestProductResponse fakeResponse = new ItemRequestProductResponse(
                1L, 1L, "Parafuso Atualizado", "KG", 20.0, "CONCLUIDO", "Informações atualizadas"
        );

        when(updateRequestProductService.update(1L, updateRequest)).thenReturn(fakeResponse);

        ItemRequestProductResponse result = itemRequestProductService.updateRequestProduct(updateRequest, 1L);

        assertNotNull(result);
        assertEquals(1L, result.requestId());
        assertEquals("Parafuso Atualizado", result.productName());
        assertEquals(20.0, result.quantity());
        assertEquals("CONCLUIDO", result.statusName());
        verify(updateRequestProductService, times(1)).update(1L, updateRequest);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar item de produto com ID inexistente")
    void shouldThrowExceptionWhenUpdatingItemRequestProductByNonExistentId() {
        ItemRequestProductRequest updateRequest = new ItemRequestProductRequest(
                1L, "Produto X", "UN", 5.0, "EM_ANDAMENTO", "Sem observações"
        );

        when(updateRequestProductService.update(99L, updateRequest))
                .thenThrow(new ItemRequestProductNotFoundException());

        assertThrows(ItemRequestProductNotFoundException.class, () ->
                itemRequestProductService.updateRequestProduct(updateRequest, 99L)
        );

        verify(updateRequestProductService, times(1)).update(99L, updateRequest);
    }

    // ───────────────────────────── DELETE ─────────────────────────────

    @Test
    @DisplayName("Deve deletar um item de produto por ID com sucesso")
    void shouldDeleteItemRequestProduct() {
        Long idToDelete = 1L;

        assertDoesNotThrow(() -> itemRequestProductService.deleteRequestProduct(idToDelete));

        verify(deleteRequestProductService, times(1)).delete(idToDelete);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar item de produto com ID inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentItemRequestProduct() {
        Long nonExistentId = 99L;

        doThrow(new ItemRequestProductNotFoundException())
                .when(deleteRequestProductService).delete(nonExistentId);

        assertThrows(ItemRequestProductNotFoundException.class, () ->
                itemRequestProductService.deleteRequestProduct(nonExistentId)
        );

        verify(deleteRequestProductService, times(1)).delete(nonExistentId);
    }
}
