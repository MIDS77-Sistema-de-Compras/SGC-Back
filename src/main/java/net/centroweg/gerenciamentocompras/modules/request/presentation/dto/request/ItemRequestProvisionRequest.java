package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * DTO de entrada para criação e atualização de um {@link ItemRequestProvision}.
 * @param requestId identificador da requisição à qual o item pertence, não pode ser nulo e deve ser positivo.
 * @param provisionId identificador do serviço referente ao item, não pode ser nulo e deve ser positivo.
 * @param statusId identificador da situação atual do item, não pode ser nulo e deve ser positivo.
 * @param additionalInformation informações adicionais sobre o item.
 */
public record ItemRequestProvisionRequest(
    @NotNull(message = "O ID da solicitação não pode ser nulo e nem vazio!")
    @Positive(message = "O ID da solicitação deve ser maior ou igual a zero!")
    Long requestId,

    @NotNull(message = "O ID do serviço não pode ser nulo e nem vazio!")
    @Positive(message = "O ID do serviço deve ser maior ou igual a zero!")
    Long provisionId,
    
    @NotNull(message = "O ID do status não pode ser nulo e nem vazio!")
    @Positive(message = "O ID do status deve ser maior ou igual a zero!")
    Long statusId,

    String additionalInformation
) {}
