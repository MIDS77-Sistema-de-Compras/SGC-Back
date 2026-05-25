package net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProvisionRequest(
    @NotBlank(message="O nome do serviço não pode estar vazio.")
    @NotNull(message="O nome do serviço não pode ser nulo.")
    String name,

    @NotNull(message="O valor total do serviço não pode ser nulo.")
    Double totalValue,

    @NotBlank(message="A descrição do serviço não pode ser ")
    String description
) {}
