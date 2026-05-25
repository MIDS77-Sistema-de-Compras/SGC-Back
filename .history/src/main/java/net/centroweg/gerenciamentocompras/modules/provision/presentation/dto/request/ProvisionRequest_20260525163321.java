package net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProvisionRequest(
    @NotBlank(message="O nome do serviço não pode estar vazio.")
    @NotNull(message="The provision's name cannot be null.")
    String name,

    @NotNull(message="The provision's total value cannot be null.")
    Double totalValue,

    @NotBlank(message="The provision's description cannot be blank.")
    String description
) {}
