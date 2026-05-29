package net.centroweg.gerenciamentocompras.modules.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.IProductMapper;

@ExtendWith(MockitoExtension.class)
class FindProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IProductMapper productMapper;

    @InjectMocks
    private FindProductService findProductService;

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    void mustFindProductByIdSuccessfully() {
        Long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .name("Produto A")
                .description("Descrição A")
                .price(10.0)
                .type("Tipo A")
                .code("COD01")
                .build();
        ProductResponse expectedResponse = new ProductResponse(productId, "Produto A", "Descrição A", 10.0, "Tipo A", "COD01");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(expectedResponse);

        ProductResponse response = findProductService.findById(productId);

        assertNotNull(response);
        assertEquals(productId, response.id());
        assertEquals("Produto A", response.name());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("Deve lançar ProductNotFoundException ao buscar ID inexistente")
    void mustThrowProductNotFoundExceptionWhenIdDoesNotExist() {
        Long productId = 99L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> findProductService.findById(productId));

        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Deve listar todos os produtos com sucesso")
    void mustFindAllProductsSuccessfully() {
        Product p1 = Product.builder().id(1L).name("P1").price(10.0).type("T1").code("C1").build();
        Product p2 = Product.builder().id(2L).name("P2").price(20.0).type("T2").code("C2").build();
        List<Product> products = List.of(p1, p2);

        ProductResponse r1 = new ProductResponse(1L, "P1", null, 10.0, "T1", "C1");
        ProductResponse r2 = new ProductResponse(2L, "P2", null, 20.0, "T2", "C2");
        List<ProductResponse> expectedResponseList = List.of(r1, r2);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toResponseList(products)).thenReturn(expectedResponseList);

        List<ProductResponse> response = findProductService.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("P1", response.get(0).name());
        assertEquals("P2", response.get(1).name());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar produtos por nome com sucesso")
    void mustFindProductsByNameSuccessfully() {
        String nameQuery = "PROD";
        Product p1 = Product.builder().id(1L).name("PRODUTO A").price(10.0).type("T1").code("C1").build();
        List<Product> products = List.of(p1);

        ProductResponse r1 = new ProductResponse(1L, "PRODUTO A", null, 10.0, "T1", "C1");
        List<ProductResponse> expectedResponseList = List.of(r1);

        when(productRepository.findByNameContainingIgnoreCase(nameQuery)).thenReturn(products);
        when(productMapper.toResponseList(products)).thenReturn(expectedResponseList);

        List<ProductResponse> response = findProductService.findByName(nameQuery);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("PRODUTO A", response.get(0).name());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase(nameQuery);
    }
}
