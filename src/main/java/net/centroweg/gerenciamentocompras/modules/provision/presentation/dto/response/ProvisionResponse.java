package net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response;

public record ProvisionResponse(
    Long id,
    String name,
    Double totalValue,
    String description
) {}
