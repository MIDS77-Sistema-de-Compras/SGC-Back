package net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;

/**
 * DTO de saída com os dados de um {@link Provision}.
 * @param id identificador do serviço.
 * @param name nome do serviço.
 * @param totalValue valor total do serviço em reais(BRL).
 * @param description descrição do serviço.
 */
public record ProvisionResponse(
    Long id,

    String name,

    Double totalValue,

    String description
) {}
