package net.centroweg.gerenciamentocompras.modules.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private CreateProductService createProductService;

    @Mock
    private FindProductService findProductService;

    @Mock
    private UpdateProductService updateProductService;

    @Mock
    private DeleteProductService deleteProductService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Deve delegar chamada de criação para o CreateProductService")
    void mustDelegateCreateSuccessfully() {
        CreateProductRequest request = new CreateProductRequest("P", "D", 1.0, "T", "C");
        ProductResponse expected = new ProductResponse(1L, "P", "D", 1.0, "T", "C");

        when(createProductService.execute(request)).thenReturn(expected);

        ProductResponse response = productService.create(request);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(createProductService, times(1)).execute(request);
    }

    @Test
    @DisplayName("Deve delegar chamada de busca por ID para o FindProductService")
    void mustDelegateFindByIdSuccessfully() {
        Long id = 1L;
        ProductResponse expected = new ProductResponse(id, "P", "D", 1.0, "T", "C");

        when(findProductService.findById(id)).thenReturn(expected);

        ProductResponse response = productService.findById(id);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(findProductService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve delegar chamada de busca total sem filtro para o FindProductService.findAll")
    void mustDelegateFindAllWithoutFilterSuccessfully() {
        List<ProductResponse> expected = List.of(new ProductResponse(1L, "P", "D", 1.0, "T", "C"));

        when(findProductService.findAll()).thenReturn(expected);

        List<ProductResponse> response = productService.findAll(null);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(findProductService, times(1)).findAll();
        verify(findProductService, never()).findByName(anyString());
    }

    @Test
    @DisplayName("Deve delegar chamada de busca por nome com filtro para o FindProductService.findByName")
    void mustDelegateFindAllWithNameFilterSuccessfully() {
        String filterName = "P";
        List<ProductResponse> expected = List.of(new ProductResponse(1L, "P", "D", 1.0, "T", "C"));

        when(findProductService.findByName(filterName)).thenReturn(expected);

        List<ProductResponse> response = productService.findAll(filterName);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(findProductService, times(1)).findByName(filterName);
        verify(findProductService, never()).findAll();
    }

    @Test
    @DisplayName("Deve delegar chamada de atualização para o UpdateProductService")
    void mustDelegateUpdateSuccessfully() {
        Long id = 1L;
        UpdateProductRequest request = new UpdateProductRequest("P", "D", 1.0, "T", "C");
        ProductResponse expected = new ProductResponse(id, "P", "D", 1.0, "T", "C");

        when(updateProductService.execute(id, request)).thenReturn(expected);

        ProductResponse response = productService.update(id, request);

        assertNotNull(response);
        assertEquals(expected, response);
        verify(updateProductService, times(1)).execute(id, request);
    }

    @Test
    @DisplayName("Deve delegar chamada de exclusão para o DeleteProductService")
    void mustDelegateDeleteSuccessfully() {
        Long id = 1L;

        assertDoesNotThrow(() -> productService.delete(id));

        verify(deleteProductService, times(1)).execute(id);
    }
}
