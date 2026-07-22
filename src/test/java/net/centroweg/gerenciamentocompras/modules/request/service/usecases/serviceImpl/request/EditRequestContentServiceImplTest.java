package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.service.api.CrPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.RequestAttachment;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.InvalidAttachmentException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.EditRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestProductItemRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestAttachmentRemovedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;

@ExtendWith(MockitoExtension.class)
class EditRequestContentServiceImplTest {

    @Mock private RequestRepository requestRepository;
    @Mock private CrPublicApi crPublicApi;
    @Mock private CurrentUserService currentUserService;
    @Mock private RequestBusinessRuleValidator validator;
    @Mock private RequestItemsAssembler requestItemsAssembler;
    @Mock private RequestMapper requestMapper;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private EditRequestContentServiceImpl service;

    @Test
    void shouldReplaceItemsCrAndRemoveOnlyDiscardedAttachments() {
        User currentUser = new User();
        currentUser.setId(10L);
        Status status = new Status("AGUARDANDO_APROVACAO", "Pendente");
        CrBranch oldCr = new CrBranch();
        oldCr.setId(1L);
        CrBranch newCr = new CrBranch();
        newCr.setId(2L);

        Request request = new Request();
        request.setId(100L);
        request.setActive(true);
        request.setStatus(status);
        request.setCrBranch(oldCr);
        request.getAttachments().add(attachment(11L, "keep", request));
        request.getAttachments().add(attachment(12L, "remove", request));

        RequestProductItemRequest product = new RequestProductItemRequest(
                "Papel A4", "75g", "Unidade", 2.0, "Uso em sala"
        );
        EditRequestRequest dto = new EditRequestRequest(2L, List.of(product), null, List.of(11L));
        RequestResponse response = org.mockito.Mockito.mock(RequestResponse.class);

        when(requestRepository.findById(100L)).thenReturn(Optional.of(request));
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(crPublicApi.findCrBranchById(2L)).thenReturn(Optional.of(newCr));
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toDTO(request)).thenReturn(response);

        assertEquals(response, service.edit(100L, dto));
        assertEquals(newCr, request.getCrBranch());
        assertEquals(List.of(11L), request.getAttachments().stream().map(RequestAttachment::getId).toList());

        verify(validator).validateCanEditContent(request, currentUser);
        verify(requestItemsAssembler).replaceItems(request, status, List.of(product), null);

        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        RequestAttachmentRemovedEvent event = (RequestAttachmentRemovedEvent) eventCaptor.getValue();
        assertEquals("remove", event.publicId());
    }

    @Test
    void shouldRejectRetainedAttachmentFromAnotherRequest() {
        User currentUser = new User();
        Request request = new Request();
        request.setId(100L);
        request.setActive(true);
        request.setStatus(new Status("AGUARDANDO_APROVACAO", "Pendente"));
        request.setCrBranch(new CrBranch());
        request.getAttachments().add(attachment(11L, "keep", request));

        EditRequestRequest dto = new EditRequestRequest(
                2L,
                List.of(new RequestProductItemRequest("Papel", null, "Unidade", 1.0, "Info")),
                null,
                List.of(999L)
        );

        when(requestRepository.findById(100L)).thenReturn(Optional.of(request));
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(crPublicApi.findCrBranchById(2L)).thenReturn(Optional.of(new CrBranch()));

        assertThrows(InvalidAttachmentException.class, () -> service.edit(100L, dto));
        verify(validator).validateCanEditContent(request, currentUser);
        verify(requestItemsAssembler).replaceItems(eq(request), eq(request.getStatus()), any(), eq(null));
    }

    private RequestAttachment attachment(Long id, String publicId, Request request) {
        RequestAttachment attachment = new RequestAttachment();
        attachment.setId(id);
        attachment.setPublicId(publicId);
        attachment.setResourceType("raw");
        attachment.setRequest(request);
        return attachment;
    }
}
