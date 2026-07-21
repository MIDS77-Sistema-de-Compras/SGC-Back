package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.service.api.CrPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.RequestAttachment;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.InvalidAttachmentException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.EditRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestAttachmentRemovedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;

@Service
@RequiredArgsConstructor
public class EditRequestContentServiceImpl {

    private final RequestRepository requestRepository;
    private final CrPublicApi crPublicApi;
    private final CurrentUserService currentUserService;
    private final RequestBusinessRuleValidator validator;
    private final RequestItemsAssembler requestItemsAssembler;
    private final RequestMapper requestMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public RequestResponse edit(Long requestId, EditRequestRequest dto) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(RequestNotFoundException::new);
        User currentUser = currentUserService.getCurrentUser();
        validator.validateCanEditContent(request, currentUser);

        CrBranch crBranch = crPublicApi.findCrBranchById(dto.crBranchId())
                .orElseThrow(() -> new CrBranchNotFoundException(dto.crBranchId()));

        request.setCrBranch(crBranch);
        requestItemsAssembler.replaceItems(
                request,
                request.getStatus(),
                dto.products(),
                dto.provisions()
        );
        removeDiscardedAttachments(request, dto.retainedAttachmentIds());

        return requestMapper.toDTO(requestRepository.save(request));
    }

    private void removeDiscardedAttachments(Request request, List<Long> retainedIds) {
        if (retainedIds == null) return;

        Set<Long> retained = new HashSet<>(retainedIds);
        Set<Long> existing = request.getAttachments().stream()
                .map(RequestAttachment::getId)
                .collect(java.util.stream.Collectors.toSet());

        if (!existing.containsAll(retained)) {
            throw new InvalidAttachmentException("Um ou mais anexos não pertencem à solicitação.");
        }

        request.getAttachments().removeIf(attachment -> {
            if (retained.contains(attachment.getId())) return false;

            eventPublisher.publishEvent(new RequestAttachmentRemovedEvent(
                    attachment.getPublicId(),
                    attachment.getResourceType()
            ));
            return true;
        });
    }
}
