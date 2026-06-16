package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

public record ItemRequestProvisionResponse(
    Long id,
    Long requestId,
    Long provisionId,
    String statusName,
    String additionalInformation
) {}
