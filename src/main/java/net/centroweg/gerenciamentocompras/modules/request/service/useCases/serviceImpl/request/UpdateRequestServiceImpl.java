package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestAlreadyApprovedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateRequestServiceImpl {

    private final RequestRepository requestRepository;
    private final StatusRepository statusRepository;
    private final CrBranchRepository crBranchRepository;
    private final RequestMapper requestMapper;
    private final NotificationService notificationService;
    private final CurrentUserService currentUserService;
    private final RequestBusinessRuleValidator validator;

    public RequestResponse updateRequest(UpdateRequestRequest requestDTO, Long id){
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());

        User currentUser = currentUserService.getCurrentUser();
        validator.validateCanEdit(request, currentUser);

        Status status = statusRepository.findByNameIgnoreCase(requestDTO.statusName())
                .orElseThrow(() -> new StatusNotFoundException());

        CrBranch crBranch = crBranchRepository.findById(requestDTO.crBranchId())
                .orElseThrow(() -> new CrBranchNotFoundException(requestDTO.crBranchId()));

        if (status.getName().equalsIgnoreCase("Aprovado")) {
            throw new RequestAlreadyApprovedException();
        }

        boolean crBranchChanging = !request.getCrBranch().getId().equals(crBranch.getId());
        if (crBranchChanging) {
            validator.validateCrCanBeChanged(request, currentUser);
        }

        boolean statusChange = !request.getStatus().getId().equals(status.getId());

        request.setStatus(status);
        request.setCrBranch(crBranch);
        request.setUpdatedAt(LocalDateTime.now());

        Request savedRequest = requestRepository.save(request);

        if (statusChange && crBranch.getResponsibleUsers() != null) {
            for (User responsible : crBranch.getResponsibleUsers()) {
                notificationService.createNotification(new NotificationRequest(
                        "Status da solicitação atualizado",
                        "A solicitação #" + savedRequest.getId() + " teve o status alterado para " + status.getName() + ".",
                        responsible.getId(),
                        savedRequest.getId()
                ));
            }
        }

        return requestMapper.toDTO(savedRequest);
    }
}