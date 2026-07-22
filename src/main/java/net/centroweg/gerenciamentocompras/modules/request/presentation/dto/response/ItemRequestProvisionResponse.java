package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * DTO de saída com os dados de um {@link ItemRequestProvision}.
 * @param id identificador do item de provisão da solicitação.
 * @param requestId identificador da solicitação.
 * @param provisionId identificador da provisão.
 * @param statusName nome do status.
 * @param additionalInformation informações adicionais do item.
 */
public record ItemRequestProvisionResponse(
    Long id,

    Long requestId,

    Long provisionId,

    String statusName,

    String additionalInformation
) {}
