package net.centroweg.gerenciamentocompras.modules.request.service.mapper.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import org.springframework.stereotype.Component;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.RequestAttachment;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestAttachmentResponse;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

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
        List<RequestAttachmentResponse> attachments =
                request.getAttachments()
                        .stream()
                        .map(this::toAttachmentDTO)
                        .toList();

        User requester = request.getCreatedByUsers().isEmpty()
                ? null
                : request.getCreatedByUsers().get(0);

        return new RequestResponse(
                request.getId(),
                request.getRequestDate(),
                request.getUpdatedAt(),
                request.getCrBranch().getId(),
                request.getStatus().getName(),
                request.getFeedback(),
                requester != null ? requester.getName() : null,
                requester != null ? requester.getExtensionNumber() : null,
                attachments
        );
    }

    public RequestAttachmentResponse toAttachmentDTO(
            RequestAttachment attachment
    ) {
        return new RequestAttachmentResponse(
                attachment.getId(),
                attachment.getOriginalName(),
                attachment.getUrl(),
                attachment.getContentType(),
                attachment.getSize(),
                attachment.getUploadedAt()
        );
    }

    public List<RequestResponse> toDTOList(List<Request> requests){
        return requests
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
