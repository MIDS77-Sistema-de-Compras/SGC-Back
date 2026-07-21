package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.itemRequestProduct;

import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProductAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.itemRequestProduct.ItemRequestProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRequestProductServiceTest {

    @Mock private ItemRequestProductRepository itemRepository;
    @Mock private RequestRepository requestRepository;
    @Mock private StatusRepository statusRepository;
    @Mock private ItemRequestProductMapper mapper;
    @Mock private RequestPublicApi requestPublicApi;

    @Test
    void shouldRejectProductAlreadyAddedToTheSameRequestBeforeSaving() {
        CreateRequestProductService service = new CreateRequestProductService(
                itemRepository, requestRepository, statusRepository, mapper, requestPublicApi
        );
        Request request = new Request();
        request.setId(1L);
        Product product = Product.builder().id(2L).name("Parafuso").build();
        ItemRequestProductRequest dto = new ItemRequestProductRequest(1L, "Parafuso", "UN", 1.0, "Pendente", "Obs");
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestPublicApi.findProuctByNameIgnoreCase("Parafuso")).thenReturn(Optional.of(product));
        when(itemRepository.existsByRequestIdAndProductId(1L, 2L)).thenReturn(true);

        assertThrows(ItemRequestProductAlreadyExistsException.class, () -> service.create(dto));

        verify(itemRepository, never()).save(any());
        verifyNoFurtherLookup();
    }

    private void verifyNoFurtherLookup() {
        verify(statusRepository, never()).findByNameIgnoreCase(any());
    }
}
