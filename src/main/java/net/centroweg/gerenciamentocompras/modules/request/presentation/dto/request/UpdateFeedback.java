package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * DTO de entrada para atualização do feedback de uma {@link Request}.
 * @param feedback texto de feedback da solicitação.
 */
public record UpdateFeedback(
        String feedback
) {
}
