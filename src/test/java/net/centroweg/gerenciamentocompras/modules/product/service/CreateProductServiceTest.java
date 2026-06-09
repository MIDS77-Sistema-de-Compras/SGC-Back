package net.centroweg.gerenciamentocompras.modules.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.IProductMapper;

@ExtendWith(MockitoExtension.class)
class CreateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IProductMapper productMapper;

    @InjectMocks
    private CreateProductService createProductService;

    @Test
    @DisplayName("Deve cadastrar um produto com sucesso")
    void mustCreateProductSuccessfully() {
        CreateProductRequest request = new CreateProductRequest("Produto A", "Descrição A", 10.0, "Tipo A", "COD01");
        
        Product savedProduct = Product.builder()
                .id(1L)
                .name("Produto A")
                .description("Descrição A")
                .price(10.0)
                .type("Tipo A")
                .code("COD01")
                .build();
                
        ProductResponse expectedResponse = new ProductResponse(1L, "Produto A", "Descrição A", 10.0, "Tipo A", "COD01");

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.toResponse(savedProduct)).thenReturn(expectedResponse);

        ProductResponse response = createProductService.execute(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Produto A", response.name());
        assertEquals("Descrição A", response.description());
        assertEquals(10.0, response.price());
        assertEquals("Tipo A", response.type());
        assertEquals("COD01", response.code());

        verify(productRepository, times(1)).save(any(Product.class));
    }
}
