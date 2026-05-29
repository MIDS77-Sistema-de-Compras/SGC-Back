package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.CreateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRequestServiceImpl {

    private final RequestRepository repository;
    private final CrBranchRepository crBranchRepository;
    private final StatusRepository statusRepository;
    private final RequestMapper mapper;

    public RequestResponse createRequest(CreateRequestRequest request){
        Status status = statusRepository.findByName(request.statusName())
                .orElseThrow(() -> new RuntimeException("Status não foi encontrado!"));
        CrBranch crBranch = crBranchRepository.findById(request.crBranchId())
                .orElseThrow(() -> new RuntimeException("CR branch não foi encontrada!"));
        Request requestSave = mapper.toEntity(request, crBranch, status);
        return mapper.toDTO(repository.save(requestSave));
    }
}
