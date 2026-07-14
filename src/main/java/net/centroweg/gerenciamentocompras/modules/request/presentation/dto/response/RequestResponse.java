package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;


import net.centroweg.gerenciamentocompras.modules.request.domain.RequestStatusCategory;

import java.time.LocalDateTime;
import java.util.List;

public record RequestResponse(
        Long id,
        LocalDateTime requestDate,
        LocalDateTime updatedAt,
        Long crBranchId,
        String statusName,
        RequestStatusCategory statusCategory,
        String feedback,
        String requesterName,
        String requesterExtension,
        List<RequestAttachmentResponse> attachments,
        List<ItemRequestProductResponse> products,
        List<ItemRequestProvisionResponse> provisions
) {}