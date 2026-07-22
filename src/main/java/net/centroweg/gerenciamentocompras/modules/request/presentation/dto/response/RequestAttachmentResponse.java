package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

import java.time.LocalDateTime;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.RequestAttachment;

/**
 * DTO de saída com os dados de um {@link RequestAttachment}.
 * @param id identificador do anexo.
 * @param originalName nome original do arquivo.
 * @param url URL de acesso ao arquivo.
 * @param contentType tipo de conteúdo do arquivo.
 * @param size tamanho do arquivo em bytes.
 * @param uploadedAt data e hora do upload.
 */
public record RequestAttachmentResponse(
        Long id,

        String originalName,

        String url,

        String contentType,

        Long size,

        LocalDateTime uploadedAt
) {
}