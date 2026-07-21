package net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;

/**
 * DTO de entrada para criação e atualização de um {@link Provision}.
 * @param name nome do serviço, não pode ser nulo ou vazio.
 * @param totalValue valor total do serviço em reais(BRL), não pode ser nulo e deve ser positivo.
 * @param description descrição do serviço, não pode ser nula ou vazia.
 */
public record ProvisionRequest(
    @NotBlank(message="O nome do serviço não deve ser nulo e nem vazio!")
    String name,
    @NotNull(message="O valor total do serviço não deve ser nulo e nem vazio!")
    @Positive(message = "O valor total do serviço deve ser maior que zero!")
    Double totalValue,
    @NotBlank(message="A descrição do serviço não deve ser nula e nem vazia!")
    String description
) {}
