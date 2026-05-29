package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestAlreadyApprovedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.CreateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateRequestServiceImpl {

    private final RequestRepository repository;
    private final StatusRepository statusRepository;
    private final CrBranchRepository crBranchRepository;
    private final RequestMapper mapper;

    public RequestResponse updateRequest(CreateRequestRequest request, Long id){
        Request requestSave = repository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());
        Status status = statusRepository.findByNameIgnoreCase(request.statusName())
                        .orElseThrow(() -> new StatusNotFoundException());
        CrBranch crBranch = crBranchRepository.findById(request.crBranchId())
                        .orElseThrow(() -> new CrBranchNotFoundException(request.crBranchId()));
        if(status.getName().toLowerCase() == "aprovada"){
            throw new RequestAlreadyApprovedException();
        }
        requestSave.setStatus(status);
        requestSave.setCrBranch(crBranch);
        requestSave.setUpdatedAt(LocalDateTime.now());
        return mapper.toDTO(repository.save(requestSave));
    }
}
