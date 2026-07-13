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
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestApprovedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public RequestResponse updateStatus(Long id, UpdateRequestStatus dto) {
        Request request = requestRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);

        User currentUser = currentUserService.getCurrentUser();
        validator.validateCanUpdateStatus(request, currentUser);

        Status newStatus = statusRepository.findByNameIgnoreCase(dto.statusName())
                .orElseThrow(StatusNotFoundException::new);

        boolean isRefused = isStatus(newStatus, REFUSED_STATUS);
        boolean isApproved = isStatus(newStatus, APPROVED_STATUS);

        boolean statusChanged = !request.getStatus().getId().equals(newStatus.getId());

        request.setStatus(newStatus);

        if (isRefused && StringUtils.hasText(dto.justification())) {
            request.setFeedback(dto.justification().trim());
        }

        Request savedRequest = requestRepository.save(request);

        if (statusChanged && isApproved) {
            eventPublisher.publishEvent(new RequestApprovedEvent(savedRequest.getId()));
        }

        if (statusChanged && (isApproved || isRefused)) {
            notifyRequester(savedRequest, isApproved, dto.justification());
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