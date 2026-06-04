package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

public record ItemRequestProductRequest(

        long requestId,
        String productName,
        String measurementUnit,
        double quantity,
        String statusName

) {
}
