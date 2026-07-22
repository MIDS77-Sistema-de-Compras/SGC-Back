package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * DTO de saída com os dados de uma {@link Request}.
 * @param id identificador da solicitação.
 * @param requestDate data e hora de criação da solicitação.
 * @param updatedAt data e hora da última atualização da solicitação.
 * @param crBranchId identificador da filial/CR.
 * @param statusName nome do status.
 * @param feedback texto de feedback da solicitação.
 * @param requesterName nome do solicitante.
 * @param requesterExtension ramal do solicitante.
 * @param attachments lista de anexos da solicitação.
 */
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