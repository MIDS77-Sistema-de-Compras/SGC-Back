package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;


import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

public record ItemRequestProductResponse(
        long itemRequestProduct,
        Request request,
        String productName,
        String measurementUnit,
        double quantity,
        String statusName,
        String additionalInformations
) {
}
