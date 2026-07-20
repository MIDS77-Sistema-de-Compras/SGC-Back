package net.centroweg.gerenciamentocompras.modules.request.service.mapper.request;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.RequestAttachment;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestAttachmentResponse;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestListItemResponse;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

@Component
@RequiredArgsConstructor
public class RequestMapper {

    private final StatusRepository repositorySt;
    private final RequestStatusCategoryResolver statusCategoryResolver;

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

        List<ItemRequestProductResponse> products = request.getItemRequestProducts()
                .stream()
                .map(this::toProductDTO)
                .toList();

        List<ItemRequestProvisionResponse> provisions = request.getItemRequestProvisions()
                .stream()
                .map(this::toProvisionDTO)
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
                statusCategoryResolver.resolve(request.getStatus().getName()),
                request.getFeedback(),
                requester != null ? requester.getName() : null,
                requester != null ? requester.getExtensionNumber() : null,
                attachments,
                products,
                provisions
        );
    }

    /**
     * Converte para o DTO enxuto de listagem, sem tocar em anexos, itens de serviço
     * ou solicitantes — apenas o que a tela de lista exibe.
     */
    public RequestListItemResponse toListItemDTO(Request request) {
        List<String> productNames = request.getItemRequestProducts()
                .stream()
                .map(item -> item.getProduct() != null ? item.getProduct().getName() : null)
                .toList();

        String crCode = request.getCrBranch() != null && request.getCrBranch().getCr() != null
                ? request.getCrBranch().getCr().getCode()
                : null;

        return new RequestListItemResponse(
                request.getId(),
                request.getRequestDate(),
                request.getUpdatedAt(),
                crCode,
                request.getStatus().getName(),
                statusCategoryResolver.resolve(request.getStatus().getName()),
                productNames
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

    private ItemRequestProductResponse toProductDTO(ItemRequestProduct item) {
        return new ItemRequestProductResponse(
                item.getId(),
                item.getRequest().getId(),
                item.getProduct() != null ? item.getProduct().getName() : null,
                item.getMeasurementUnit() != null ? item.getMeasurementUnit().getName() : null,
                item.getQuantity(),
                item.getStatus_id() != null ? item.getStatus_id().getName() : null,
                item.getAdditionalInformations()
        );
    }

    private ItemRequestProvisionResponse toProvisionDTO(ItemRequestProvision item) {
        return new ItemRequestProvisionResponse(
                item.getId(),
                item.getRequest().getId(),
                item.getProvision() != null ? item.getProvision().getId() : null,
                item.getStatus() != null ? item.getStatus().getName() : null,
                item.getAdditionalInformation()
        );
    }

    public List<RequestResponse> toDTOList(List<Request> requests){
        return requests
                .stream()
                .map(this::toDTO)
                .toList();
    }
}