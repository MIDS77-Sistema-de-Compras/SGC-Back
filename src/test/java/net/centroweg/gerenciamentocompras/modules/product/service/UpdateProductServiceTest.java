package net.centroweg.gerenciamentocompras.modules.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.IProductMapper;

@ExtendWith(MockitoExtension.class)
class UpdateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IProductMapper productMapper;

    @InjectMocks
    private UpdateProductService updateProductService;

    @Test
    @DisplayName("Deve atualizar os dados de um produto existente com sucesso")
    void mustUpdateProductSuccessfully() {
        Long productId = 1L;
        UpdateProductRequest request = new UpdateProductRequest("Produto Alterado", "Descrição Alterada", 15.0, "Tipo Alterado", "COD01-ALT");
        
        Product existingProduct = Product.builder()
                .id(productId)
                .name("Produto A")
                .description("Descrição A")
                .price(10.0)
                .type("Tipo A")
                .code("COD01")
                .build();

        ProductResponse expectedResponse = new ProductResponse(productId, "Produto Alterado", "Descrição Alterada", 15.0, "Tipo Alterado", "COD01-ALT");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);
        when(productMapper.toResponse(existingProduct)).thenReturn(expectedResponse);

        ProductResponse response = updateProductService.execute(productId, request);

        assertNotNull(response);
        assertEquals(productId, response.id());
        assertEquals("Produto Alterado", existingProduct.getName());
        assertEquals("Descrição Alterada", existingProduct.getDescription());
        assertEquals(15.0, existingProduct.getPrice());
        assertEquals("Tipo Alterado", existingProduct.getType());
        assertEquals("COD01-ALT", existingProduct.getCode());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    @DisplayName("Deve lançar ProductNotFoundException ao tentar atualizar produto inexistente")
    void mustThrowProductNotFoundExceptionWhenUpdatingNonExistentProduct() {
        Long productId = 99L;
        UpdateProductRequest request = new UpdateProductRequest("Produto Alterado", "Descrição Alterada", 15.0, "Tipo Alterado", "COD01-ALT");

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> updateProductService.execute(productId, request));

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any());
    }
}
