package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

public record ItemRequestProductResponse(
        Long itemRequestProduct,
        Long requestId,
        String productName,
        String variation,
        String measurementUnit,
        Double quantity,
        String statusName,
        String additionalInformations
) {
}
