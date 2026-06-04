package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

public record ItemRequestProductResponse(
        long itemRequestProduct,
        long requestId,
        String productName,
        String measurementUnit,
        double quantity,
        String statusName
) {
}
