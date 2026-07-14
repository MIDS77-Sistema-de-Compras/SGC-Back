package net.centroweg.gerenciamentocompras.modules.request.service.api.dto;

public record RequestProductEmailData(
        String name,
        String code,
        Double quantity,
        String measurementUnit,
        String additionalInformation
) {
}
