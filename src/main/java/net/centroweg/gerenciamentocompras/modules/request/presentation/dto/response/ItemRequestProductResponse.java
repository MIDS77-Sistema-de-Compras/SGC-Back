package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;

/**
 * DTO de saída com os dados de um {@link ItemRequestProduct}.
 * @param itemRequestProduct identificador do item de produto da solicitação.
 * @param requestId identificador da solicitação.
 * @param productName nome do produto.
 * @param measurementUnit unidade de medida do produto.
 * @param quantity quantidade do produto.
 * @param statusName nome do status.
 * @param additionalInformations informações adicionais do item.
 */
public record ItemRequestProductResponse(
        Long itemRequestProduct,

        Long requestId,

        String productName,

        String measurementUnit,

        Double quantity,

        String statusName,

        String additionalInformations
) {
}
