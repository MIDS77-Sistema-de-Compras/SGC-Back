package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.service.api.CrPublicApi;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.NotificationService;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestApprovedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;

@Service
@RequiredArgsConstructor
public class CreateRequestServiceImpl {

    private static final String INITIAL_STATUS = "Aguardando aprovação";
    private static final String APPROVED_STATUS = "AUTO_APROVADO";
    private final RequestRepository requestRepository;
    private final CrPublicApi crPublicApi;
    private final StatusRepository statusRepository;
    private final UserPublicApi userPublicApi;
    private final RequestMapper requestMapper;
    private final NotificationService notificationService;
    private final RequestItemsAssembler requestItemsAssembler;
    private final ApplicationEventPublisher eventPublisher;

    public RequestResponse createRequest(RequestRequest request, UserPrincipal userPrincipal){

        User requester = userPublicApi.findByEmail(userPrincipal.getUsername())
                .orElseThrow(UserNotFoundException::new);

        boolean isSupervisor = isSupervisorOrCoordenador(requester);

        Status status = isSupervisor
                ? statusRepository.findByNameIgnoreCase(APPROVED_STATUS)
                .orElseThrow(StatusNotFoundException::new)
                : statusRepository.findByNameIgnoreCase(INITIAL_STATUS)
                .orElseThrow(StatusNotFoundException::new);

        CrBranch crBranch = crPublicApi.findCrBranchById(request.crBranchId())
                .orElseThrow(() -> new CrBranchNotFoundException(request.crBranchId()));

        List<User> assignedUsers = new ArrayList<>();
        assignedUsers.add(requester);
        if (request.userIds() != null) {
            request.userIds().forEach(userId ->
                    userPublicApi.findUserById(userId).ifPresent(assignedUsers::add)
            );
        }

        Request requestToSave = requestMapper.toEntity(request, crBranch, status);
        requestToSave.setCreatedByUsers(assignedUsers);
        requestItemsAssembler.addItems(
                requestToSave,
                status,
                request.products(),
                request.provisions()
        );

        Request savedRequest = requestRepository.save(requestToSave);

        if (isSupervisor) {
            eventPublisher.publishEvent(new RequestApprovedEvent(savedRequest.getId()));
        }

        if (crBranch.getResponsibleUsers() != null) {
            for (User responsible : crBranch.getResponsibleUsers()) {
                notificationService.createNotification(new NotificationRequest(
                        "Nova solicitação vinculada ao seu CR",
                        "Ha uma nova solicitacao vinculada ao seu CR " + crBranch.getCr().getName() + ".",
                        NotificationType.SOLICITACAO_VINCULADA_CR.toString(),
                        responsible.getId(),
                        savedRequest.getId()
                ));
            }
        }
        return requestMapper.toDTO(savedRequest);
    }

    private boolean isSupervisorOrCoordenador(User user) {
        if (user.getRole() == null || user.getRole().getName() == null) {
            return false;
        }

        String roleName = user.getRole().getName().trim();
        return roleName.equalsIgnoreCase(Authorities.SUPERVISOR)
                || roleName.equalsIgnoreCase(Authorities.COORDENADOR);
    }
}
