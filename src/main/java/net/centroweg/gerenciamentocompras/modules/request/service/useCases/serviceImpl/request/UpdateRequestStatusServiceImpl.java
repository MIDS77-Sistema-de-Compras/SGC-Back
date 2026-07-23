package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestRejectionJustificationRequiredException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestStatus;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Caso de uso responsável pela atualização do status de uma {@link Request}.
 */
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

    /**
     * Atualiza o status de uma solicitação existente no banco de dados.
     * @param id identificador da solicitação.
     * @param dto novos dados de status da solicitação.
     * @return solicitação já atualizada.
     * @throws RequestNotFoundException caso nenhuma solicitação seja encontrada.
     * @throws StatusNotFoundException caso nenhum status seja encontrado.
     * @throws RequestRejectionJustificationRequiredException caso a solicitação seja recusada sem justificativa.
     */
    public RequestResponse updateStatus(Long id, UpdateRequestStatus dto) {
        Request request = requestRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);

        User currentUser = currentUserService.getCurrentUser();
        validator.validateCanUpdateStatus(request, currentUser);

        Status newStatus = statusRepository.findByNameIgnoreCase(dto.statusName())
                .orElseThrow(StatusNotFoundException::new);

        boolean isRefused = isStatus(newStatus, REFUSED_STATUS);
        boolean isApproved = isStatus(newStatus, APPROVED_STATUS);

        if (isRefused && !StringUtils.hasText(dto.justification())) {
            throw new RequestRejectionJustificationRequiredException();
        }

        boolean statusChanged = !request.getStatus().getId().equals(newStatus.getId());

        request.setStatus(newStatus);

        if (isRefused) {
            request.setFeedback(dto.justification().trim());
        }

        Request savedRequest = requestRepository.save(request);

        if (statusChanged && (isApproved || isRefused)) {
            notifyRequester(savedRequest, isApproved, dto.justification());
        }

        return requestMapper.toDTO(savedRequest);
    }

    /**
     * Notifica os solicitantes sobre a aprovação ou recusa da solicitação.
     * @param request dados da solicitação.
     * @param approved indica se a solicitação foi aprovada.
     * @param justification justificativa da recusa da solicitação.
     */
    private void notifyRequester(Request request, boolean approved, String justification) {
        String title = approved
                ? "Solicitação aprovada!"
                : "Solicitação recusada!";

        String message = approved
                ? "A sua solicitação #" + request.getId() + " foi aprovada!"
                : "A sua solicitação #" + request.getId() + " foi recusada! Justificativa: " + justification;

        for (User requester : request.getCreatedByUsers()) {
            notificationService.createNotification(new NotificationRequest(
                    title,
                    message,
                    requester.getId(),
                    request.getId()
            ));
        }
    }

    /**
     * Verifica se o status informado corresponde ao status esperado, ignorando maiúsculas e minúsculas.
     * @param status dados do status.
     * @param expectedStatus nome do status esperado.
     * @return {@code true} caso o status corresponda ao esperado, {@code false} caso contrário.
     */
    private boolean isStatus(Status status, String expectedStatus) {
        return status.getName() != null
                && status.getName().trim().equalsIgnoreCase(expectedStatus);
    }
}