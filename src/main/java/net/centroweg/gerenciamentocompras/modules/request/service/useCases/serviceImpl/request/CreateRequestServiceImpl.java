package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRequestServiceImpl {

    private final RequestRepository requestRepository;
    private final CrBranchRepository crBranchRepository;
    private final StatusRepository statusRepository;
    private final RequestMapper requestMapper;
    private final NotificationService notificationService;

    public RequestResponse createRequest(RequestRequest request){
        Status status = statusRepository.findByNameIgnoreCase(request.statusName())
                .orElseThrow(() -> new StatusNotFoundException());

        status.setName("EM_ANDAMENTO");

        CrBranch crBranch = crBranchRepository.findById(request.crBranchId())
                .orElseThrow(() -> new CrBranchNotFoundException(request.crBranchId()));

        Request savedRequest = requestRepository.save(requestMapper.toEntity(request, crBranch, status));

        if (crBranch.getResponsibleUser() != null) {
            notificationService.createNotification(new NotificationRequest(
                    "Nova solicitação",
                    "Há uma nova solicitação vinculada ao seu CR " + crBranch.getCr().getName() + ".",
                    crBranch.getResponsibleUser().getId(),
                    savedRequest.getId()
            ));
        }
        return requestMapper.toDTO(savedRequest);
    }
}
