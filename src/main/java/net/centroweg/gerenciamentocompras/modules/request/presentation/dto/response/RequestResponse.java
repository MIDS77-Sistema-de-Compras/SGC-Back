package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;


import java.time.LocalDateTime;
import java.util.List;

public record RequestResponse(
        Long id,
        LocalDateTime requestDate,
        LocalDateTime updatedAt,
        Long crBranchId,
        String statusName,
        String feedback,
        String requesterName,
        String requesterExtension,
        List<RequestAttachmentResponse> attachments
) {}