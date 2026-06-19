package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemRequestProvisionRequest(
    @NotNull(message = "O ID da solicitação não pode ser nulo.")
    @Positive(message = "O ID da solicitação deve ser maior que 0.")
    Long requestId,

    @NotNull(message = "O ID do serviço não pode ser nulo.")
    @Positive(message = "O ID do serviço deve ser maior que 0")
    Long provisionId,
    
    @NotNull(message = "O ID do status não pode ser nulo.")
    @Positive(message = "O ID do status deve ser maior que 0")
    Long statusId,

    String additionalInformation
) {}
