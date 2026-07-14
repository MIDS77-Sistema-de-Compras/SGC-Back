package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestStatus;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.CompradorRequestAccessValidator;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestApprovedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateRequestStatusServiceImpl {

    private static final String REFUSED_STATUS = "recusado";
    private static final String APPROVED_STATUS = "aprovado";

    private final RequestRepository requestRepository;
    private final StatusRepository statusRepository;
    private final RequestMapper requestMapper;
    private final CurrentUserService currentUserService;
    private final RequestBusinessRuleValidator validator;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;
    private final CompradorRequestAccessValidator compradorRequestAccessValidator;

    @Transactional
    public RequestResponse updateStatus(Long id, UpdateRequestStatus dto) {
        Request request = requestRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);

        compradorRequestAccessValidator.validate(request);

        User currentUser = currentUserService.getCurrentUser();
        validator.validateCanUpdateStatus(request, currentUser);

        Status newStatus = statusRepository.findByNameIgnoreCase(dto.statusName())
                .orElseThrow(StatusNotFoundException::new);

        boolean isRefused = isStatus(newStatus, REFUSED_STATUS);

        Status previousStatus = request.getStatus();
        Long previousStatusId = previousStatus != null ? previousStatus.getId() : null;
        boolean statusChanged = !Objects.equals(previousStatusId, newStatus.getId());
        String justification = isRefused && StringUtils.hasText(dto.justification())
                ? dto.justification().trim()
                : null;

        request.setStatus(newStatus);

        if (justification != null) {
            request.setFeedback(justification);
        }

        Request savedRequest = requestRepository.save(request);

        if (statusChanged) {
            eventPublisher.publishEvent(new RequestStatusChangedEvent(
                    savedRequest.getId(),
                    previousStatusId,
                    previousStatus != null ? previousStatus.getName() : null,
                    newStatus.getId(),
                    newStatus.getName(),
                    justification,
                    currentUser.getId(),
                    currentUser.getName(),
                    LocalDateTime.now()
            ));
        }

        return requestMapper.toDTO(savedRequest);
    }

    private void notifyRequester(Request request, boolean approved, String justification) {
        String title = approved
                ? "Solicitação aprovada"
                : "Solicitação recusada";

        String message;
        if (approved) {
            message = "A sua solicitação #" + request.getId() + " foi aprovada.";
        } else {
            message = "A sua solicitação #" + request.getId() + " foi recusada.";
            if (StringUtils.hasText(justification)) {
                message += " Justificativa: " + justification;
            }
        }

        for (User requester : request.getCreatedByUsers()) {
            notificationService.createNotification(new NotificationRequest(
                    title,
                    message,
                    requester.getId(),
                    request.getId()
            ));
        }
    }

    private boolean isStatus(Status status, String expectedStatus) {
        return status.getName() != null
                && status.getName().trim().equalsIgnoreCase(expectedStatus);
    }
}