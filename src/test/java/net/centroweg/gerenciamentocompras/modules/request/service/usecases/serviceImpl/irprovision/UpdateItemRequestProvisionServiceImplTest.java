package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.irprovision;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.service.api.ProvisionPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.irprovision.ItemRequestProvisionMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
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
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateItemRequestProvisionServiceImplTest {

    @Mock private ItemRequestProvisionMapper mapper;
    @Mock private ItemRequestProvisionRepository itemRepository;
    @Mock private ProvisionPublicApi provisionPublicApi;
    @Mock private StatusRepository statusRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private RequestBusinessRuleValidator requestBusinessRuleValidator;
    @Mock private CurrentUserService currentUserService;
    @Mock private User currentUser;

    private UpdateItemRequestProvisionServiceImpl service;
    private ItemRequestProvision item;
    private Request request;
    private Provision provision;
    private Status previousStatus;
    private Status newStatus;

    @BeforeEach
    void setUp() {
        service = new UpdateItemRequestProvisionServiceImpl(
                mapper, itemRepository, provisionPublicApi, statusRepository, eventPublisher,
                requestBusinessRuleValidator, currentUserService
        );
        request = new Request();
        request.setId(10L);
        provision = new Provision("Instalacao", 100.0, "Servico");
        provision.setId(20L);
        previousStatus = status(1L, "Aprovado");
        newStatus = status(2L, "Entregue");
        item = new ItemRequestProvision(request, provision, previousStatus, "Obs");
        item.setId(99L);
    }

    @Test
    void shouldSaveAndPublishExactlyOneEventWhenProvisionStatusChanges() {
        arrangeSuccessfulUpdate(newStatus);

        service.updateItem(99L, requestDto(2L));

        verify(itemRepository).save(item);
        ArgumentCaptor<ItemStatusChangedEvent> event = ArgumentCaptor.forClass(ItemStatusChangedEvent.class);
        verify(eventPublisher).publishEvent(event.capture());
        assertThat(event.getValue().itemType()).isEqualTo(RequestItemType.PROVISION);
        assertThat(event.getValue().previousStatusName()).isEqualTo("Aprovado");
        assertThat(event.getValue().newStatusName()).isEqualTo("Entregue");
        verify(requestBusinessRuleValidator).validateCanEditItems(same(request), same(currentUser));
    }

    @Test
    void shouldNotPublishWhenProvisionStatusDoesNotChange() {
        arrangeSuccessfulUpdate(previousStatus);

        service.updateItem(99L, requestDto(1L));

        verify(itemRepository).save(item);
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void shouldNotSaveOrPublishWhenValidationFailsBeforeSave() {
        when(itemRepository.findById(99L)).thenReturn(Optional.of(item));
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        doThrow(new AcessDeniedException()).when(requestBusinessRuleValidator)
                .validateCanEditItems(request, currentUser);

        assertThrows(AcessDeniedException.class, () -> service.updateItem(99L, requestDto(2L)));

        verify(itemRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
        verifyNoInteractions(provisionPublicApi, statusRepository);
    }

    @Test
    void shouldPreserveAdditionalInformationWhenNull() {
        arrangeSuccessfulUpdate(previousStatus);
        service.updateItem(99L, requestDto(1L, null));
        assertThat(item.getAdditionalInformation()).isEqualTo("Obs");
    }

    @Test
    void shouldPreserveAdditionalInformationWhenEmpty() {
        arrangeSuccessfulUpdate(previousStatus);
        service.updateItem(99L, requestDto(1L, ""));
        assertThat(item.getAdditionalInformation()).isEqualTo("Obs");
    }

    @Test
    void shouldPreserveAdditionalInformationWhenBlank() {
        arrangeSuccessfulUpdate(previousStatus);
        service.updateItem(99L, requestDto(1L, "   "));
        assertThat(item.getAdditionalInformation()).isEqualTo("Obs");
    }

    @Test
    void shouldTrimValidAdditionalInformation() {
        arrangeSuccessfulUpdate(previousStatus);
        service.updateItem(99L, requestDto(1L, "  texto valido  "));
        assertThat(item.getAdditionalInformation()).isEqualTo("texto valido");
    }

    @Test
    void shouldValidateOriginalRequestBeforeChangingItem() {
        arrangeSuccessfulUpdate(newStatus);
        doAnswer(invocation -> {
            assertThat(item.getRequest()).isSameAs(request);
            assertThat(item.getStatus()).isSameAs(previousStatus);
            assertThat(item.getProvision()).isSameAs(provision);
            return null;
        }).when(requestBusinessRuleValidator).validateCanEditItems(request, currentUser);

        service.updateItem(99L, requestDto(2L));

        verify(requestBusinessRuleValidator).validateCanEditItems(request, currentUser);
    }

    @Test
    void shouldRejectDifferentRequestIdWithoutMovingItem() {
        when(itemRepository.findById(99L)).thenReturn(Optional.of(item));
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        ItemRequestProvisionRequest requestFromAnotherRequest = new ItemRequestProvisionRequest(
                11L, 20L, 2L, "Obs"
        );

        assertThrows(AcessDeniedException.class,
                () -> service.updateItem(99L, requestFromAnotherRequest));

        verify(requestBusinessRuleValidator).validateCanEditItems(request, currentUser);
        verifyNoInteractions(provisionPublicApi, statusRepository);
        verify(itemRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
        assertThat(item.getRequest()).isSameAs(request);
    }

    private void arrangeSuccessfulUpdate(Status status) {
        when(itemRepository.findById(99L)).thenReturn(Optional.of(item));
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(provisionPublicApi.findById(20L)).thenReturn(Optional.of(provision));
        when(statusRepository.findById(status.getId())).thenReturn(Optional.of(status));
        when(itemRepository.save(item)).thenReturn(item);
        when(mapper.toResponse(item)).thenReturn(new ItemRequestProvisionResponse(99L, 10L, 20L, status.getName(), "Obs"));
    }

    private ItemRequestProvisionRequest requestDto(Long statusId) {
        return new ItemRequestProvisionRequest(10L, 20L, statusId, "Obs");
    }

    private ItemRequestProvisionRequest requestDto(Long statusId, String additionalInformation) {
        return new ItemRequestProvisionRequest(10L, 20L, statusId, additionalInformation);
    }

    private Status status(Long id, String name) {
        Status status = new Status(name, name);
        status.setId(id);
        return status;
    }
}
