package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

import java.time.LocalDateTime;

public record RequestAttachmentResponse(
        Long id,
        String originalName,
        String url,
        String contentType,
        Long size,
        LocalDateTime uploadedAt
) {
}