package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.itemRequestProduct;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.itemRequestProduct.ItemRequestProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateRequestProductServiceTest {

    @Mock
    private ItemRequestProductRepository itemRequestProductRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private ItemRequestProductMapper itemRequestProductMapper;

    @Mock
    private RequestPublicApi requestPublicApi;

    @InjectMocks
    private CreateRequestProductService createRequestProductService;

    @Test
    void mustNotCreateTheSameProductTwiceInTheRequest() {
        Request request = new Request();
        request.setId(10L);
        Product product = Product.builder().id(20L).name("Product").build();
        ItemRequestProductRequest dto = new ItemRequestProductRequest(
                10L, "Product", "UN", 1.0, "Waiting", "details"
        );

        when(requestRepository.findById(10L)).thenReturn(Optional.of(request));
        when(requestPublicApi.findProuctByNameIgnoreCase("Product")).thenReturn(Optional.of(product));
        when(itemRequestProductRepository.existsByRequestIdAndProductId(10L, 20L)).thenReturn(true);

        assertThrows(net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProductAlreadyExistsException.class,
                () -> createRequestProductService.create(dto));

        verify(itemRequestProductRepository, never()).save(any());
    }
}
