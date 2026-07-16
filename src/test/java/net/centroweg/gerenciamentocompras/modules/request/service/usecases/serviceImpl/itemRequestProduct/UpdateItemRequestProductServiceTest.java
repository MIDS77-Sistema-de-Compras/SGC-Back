package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.itemRequestProduct;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.itemRequestProduct.ItemRequestProductMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateItemRequestProductServiceTest {

    @Mock private ItemRequestProductRepository itemRepository;
    @Mock private RequestRepository requestRepository;
    @Mock private RequestPublicApi requestPublicApi;
    @Mock private StatusRepository statusRepository;
    @Mock private ItemRequestProductMapper mapper;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private RequestBusinessRuleValidator requestBusinessRuleValidator;
    @Mock private CurrentUserService currentUserService;


    private UpdateItemRequestProductService service;
    private ItemRequestProduct item;
    private Request request;
    private Product product;
    private MeasurementUnit unit;
    private Status previousStatus;
    private Status newStatus;

    @BeforeEach
    void setUp() {
        service = new UpdateItemRequestProductService(
                itemRepository, requestRepository, requestPublicApi, statusRepository, mapper, eventPublisher,
                requestBusinessRuleValidator, currentUserService
        );
        previousStatus = status(1L, "Aprovado");
        newStatus = status(2L, "Entregue");
        item = new ItemRequestProduct();
        item.setId(99L);
        item.setStatus_id(previousStatus);
        request = new Request();
        request.setId(10L);
        product = Product.builder().id(20L).name("Parafuso").code("P-1").price(1.0).type("Insumo").build();
        unit = new MeasurementUnit(30L, "Unidade", "UN");
    }

    @Test
    void shouldSaveAndPublishExactlyOneEventWhenProductStatusChanges() {
        arrangeSuccessfulUpdate(newStatus);

        service.update(99L, requestDto("Entregue"));

        verify(itemRepository).save(item);
        ArgumentCaptor<ItemStatusChangedEvent> event = ArgumentCaptor.forClass(ItemStatusChangedEvent.class);
        verify(eventPublisher).publishEvent(event.capture());
        assertThat(event.getValue().requestId()).isEqualTo(10L);
        assertThat(event.getValue().itemId()).isEqualTo(99L);
        assertThat(event.getValue().itemType()).isEqualTo(RequestItemType.PRODUCT);
        assertThat(event.getValue().previousStatusName()).isEqualTo("Aprovado");
        assertThat(event.getValue().newStatusName()).isEqualTo("Entregue");
    }

    @Test
    void shouldNotPublishWhenProductStatusDoesNotChange() {
        arrangeSuccessfulUpdate(previousStatus);

        service.update(99L, requestDto("Aprovado"));

        verify(itemRepository).save(item);
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void shouldNotSaveOrPublishWhenValidationFailsBeforeSave() {
        when(itemRepository.findById(99L)).thenReturn(Optional.of(item));
        when(requestRepository.findById(10L)).thenReturn(Optional.of(request));
        when(requestPublicApi.findProuctByNameIgnoreCase("Parafuso")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.update(99L, requestDto("Entregue")));

        verify(itemRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    private void arrangeSuccessfulUpdate(Status status) {
        when(itemRepository.findById(99L)).thenReturn(Optional.of(item));
        when(requestRepository.findById(10L)).thenReturn(Optional.of(request));
        when(requestPublicApi.findProuctByNameIgnoreCase("Parafuso")).thenReturn(Optional.of(product));
        when(requestPublicApi.findMeasurementByNameIgnoreCase("UN")).thenReturn(Optional.of(unit));
        when(statusRepository.findByNameIgnoreCase(status.getName())).thenReturn(Optional.of(status));
        when(itemRepository.save(item)).thenReturn(item);
        when(mapper.toResponse(item)).thenReturn(new ItemRequestProductResponse(99L, 10L, "Parafuso", null, "UN", 2.0, status.getName(), "Obs"));
    }

    private ItemRequestProductRequest requestDto(String statusName) {
        return new ItemRequestProductRequest(10L, "Parafuso", null, "UN", 2.0, statusName, "Obs");
    }

    private Status status(Long id, String name) {
        Status status = new Status(name, name);
        status.setId(id);
        return status;
    }
}
