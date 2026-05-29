package net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response;

/**
 * DTO responsável por retornar dados específicos do serviço.
 * @author gabrielEFagundes
 * @version 0.1.0
 */
public record ProvisionResponse(
    Long id,
    String name,
    Double totalValue,
    String description
) {}
