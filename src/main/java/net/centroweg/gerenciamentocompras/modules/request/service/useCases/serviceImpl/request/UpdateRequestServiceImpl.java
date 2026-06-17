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
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
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

    public RequestResponse updateRequest(RequestRequest requestDTO, Long id){
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());

        Status status = statusRepository.findByNameIgnoreCase(requestDTO.statusName())
                        .orElseThrow(() -> new StatusNotFoundException());

        CrBranch crBranch = crBranchRepository.findById(requestDTO.crBranchId())
                        .orElseThrow(() -> new CrBranchNotFoundException(requestDTO.crBranchId()));

        if(status.getName().equalsIgnoreCase("Aprovado")) {
            throw new RequestAlreadyApprovedException();
        }

        boolean statusChange = !request.getStatus().getId().equals(status.getId());

        request.setStatus(status);
        request.setCrBranch(crBranch);
        request.setUpdatedAt(LocalDateTime.now());

        Request savedRequest = requestRepository.save(request);

        if (statusChange && crBranch.getResponsibleUser() != null) {
            notificationService.createNotification(new NotificationRequest(
                    "Status da solicitação atualizado",
                    "A solicitação #" + savedRequest.getId() + " teve o status alterado para " + status.getName() + ".",
                    crBranch.getResponsibleUser().getId(),
                    savedRequest.getId()
            ));
        }

        return requestMapper.toDTO(savedRequest);

    }
}
