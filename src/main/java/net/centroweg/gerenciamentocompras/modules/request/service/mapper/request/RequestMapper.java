package net.centroweg.gerenciamentocompras.modules.request.service.mapper.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestMapper {

    private final CrBranchRepository repositoryCR;
    private final StatusRepository repositorySt;

    public Request toEntity(RequestRequest request, CrBranch branch, Status status){
        Request requestSave = new Request();
        requestSave.setCrBranch(branch);
        requestSave.setStatus(status);
        return requestSave;
    }

    public RequestResponse toDTO(Request request){
        return new RequestResponse(
                request.getId(),
                request.getRequestDate(),
                request.getUpdatedAt(),
                request.getCrBranch().getId(),
                request.getStatus().getName(),
                request.getFeedback()
        );
    }

    public List<RequestResponse> toDTOList(List<Request> requests){
        return requests
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
