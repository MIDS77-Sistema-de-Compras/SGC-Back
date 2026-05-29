package net.centroweg.gerenciamentocompras.modules.product.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;

@ExtendWith(MockitoExtension.class)
class DeleteProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DeleteProductService deleteProductService;

    @Test
    @DisplayName("Deve excluir um produto com sucesso quando ID existir")
    void mustDeleteProductSuccessfullyWhenIdExists() {
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);

        assertDoesNotThrow(() -> deleteProductService.execute(productId));

        verify(productRepository, times(1)).existsById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    @DisplayName("Deve lançar ProductNotFoundException ao tentar excluir produto inexistente")
    void mustThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        Long productId = 99L;
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> deleteProductService.execute(productId));

        verify(productRepository, times(1)).existsById(productId);
        verify(productRepository, never()).deleteById(productId);
    }
}
