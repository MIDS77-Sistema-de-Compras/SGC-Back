package net.centroweg.gerenciamentocompras.modules.request.service.api.dto;

public record RequestProvisionEmailData(
        String name,
        Double totalValue,
        String description,
        String additionalInformation
) {
}
