package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

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

    @NotBlank(message = "As informações adicionais não podem estar em branco.")
    @Size(max=255, message = "Informações adicionais excedem o tamanho máximo permitido (255 caractéres).")
    String additionalInformation
) {}
