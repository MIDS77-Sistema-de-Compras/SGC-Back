package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;

/**
 * Caso de uso responsável por concluir automaticamente uma solicitação.
 *
 * <p>É acionado via {@code RequestPublicApi} pelo módulo {@code delivery}
 * quando a entrega vinculada à solicitação é finalizada ("Entregue") ou
 * cancelada ("Pedido cancelado"), marcando a solicitação como "Concluída".</p>
 */
@Service
@RequiredArgsConstructor
public class ConcludeRequestServiceImpl {

    private static final String CONCLUDED_STATUS = "Concluída";

    private final RequestRepository requestRepository;
    private final StatusRepository statusRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CurrentUserService currentUserService;

    @Transactional
    public void concludeRequest(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(RequestNotFoundException::new);

        Status concludedStatus = statusRepository.findByNameIgnoreCase(CONCLUDED_STATUS)
                .orElseThrow(StatusNotFoundException::new);

        Status previousStatus = request.getStatus();
        Long previousStatusId = previousStatus != null ? previousStatus.getId() : null;

        if (Objects.equals(previousStatusId, concludedStatus.getId())) {
            return;
        }

        request.setStatus(concludedStatus);
        Request savedRequest = requestRepository.save(request);

        User currentUser = currentUserService.getCurrentUser();

        eventPublisher.publishEvent(new RequestStatusChangedEvent(
                savedRequest.getId(),
                previousStatusId,
                previousStatus != null ? previousStatus.getName() : null,
                concludedStatus.getId(),
                concludedStatus.getName(),
                null,
                currentUser.getId(),
                currentUser.getName(),
                LocalDateTime.now()
        ));
    }
}
