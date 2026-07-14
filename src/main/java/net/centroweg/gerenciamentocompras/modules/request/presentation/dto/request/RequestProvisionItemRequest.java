package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RequestProvisionItemRequest(
        @Positive(message = "O ID do servico deve ser maior que 0.")
        Long provisionId,

        // Dados de criacao (name/totalValue/description) sao opcionais no DTO:
        // so sao exigidos ao cadastrar um servico novo, validacao feita em
        // CreateRequestServiceImpl (InsufficientProvisionDataException). Ao referenciar
        // um servico existente por provisionId eles nao precisam ser informados.
        String name,

        @Positive(message = "O valor total deve ser maior que zero.")
        Double totalValue,

        @Size(min = 10, max = 100, message = "A descrição do item deve conter entre 10 e 100 caractéres.")
        String description,

        @NotBlank(message = "As informações adicionais não podem estar em branco.")
        @Size(max = 255, message = "Informações adicionais excedem o limite máximo permitido (255 caractéres)")
        String additionalInformation
) {
}
