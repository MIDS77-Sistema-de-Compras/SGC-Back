package net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO responsável por filtrar a entrada do usuário.
 * @author gabrielEFagundes
 * @version 0.1.0
 */
public record ProvisionRequest(
    @NotBlank(message="O nome do serviço não pode estar vazio.")
    @NotNull(message="O nome do serviço não pode ser nulo.")
    String name,

    @NotNull(message="O valor total do serviço não pode ser nulo.")
    Double totalValue,

    @NotBlank(message="A descrição do serviço não pode estar vazia.")
    String description
) {}
