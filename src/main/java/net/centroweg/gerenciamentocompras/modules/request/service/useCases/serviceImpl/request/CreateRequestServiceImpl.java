package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateRequestServiceImpl {

    private final RequestRepository requestRepository;
    private final CrBranchRepository crBranchRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final NotificationService notificationService;

    public RequestResponse createRequest(RequestRequest request, UserPrincipal userPrincipal){

        Status status = statusRepository.findByNameIgnoreCase("EM_ANDAMENTO")
                .orElseThrow(StatusNotFoundException::new);

        CrBranch crBranch = crBranchRepository.findById(request.crBranchId())
                .orElseThrow(() -> new CrBranchNotFoundException(request.crBranchId()));

        User requester = userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(UserNotFoundException::new);

        List<User> assignedUsers = new ArrayList<>();
        assignedUsers.add(requester);
        request.userIds().forEach(userId ->
                userRepository.findById(userId).ifPresent(assignedUsers::add)
        );

        Request requestToSave = requestMapper.toEntity(request, crBranch, status);
        requestToSave.setCreatedByUsers(assignedUsers);

        Request savedRequest = requestRepository.save(requestToSave);

        if (crBranch.getResponsibleUsers() != null) {
            for (User responsible : crBranch.getResponsibleUsers()) {
                notificationService.createNotification(new NotificationRequest(
                        "Nova solicitação",
                        "Há uma nova solicitação vinculada ao seu CR " + crBranch.getCr().getName() + ".",
                        responsible.getId(),
                        savedRequest.getId()
                ));
            }
        }
        return requestMapper.toDTO(savedRequest);
    }

}