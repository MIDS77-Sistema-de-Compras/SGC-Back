package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;

/**
 * DTO de entrada para criação e atualização de um {@link ItemRequestProduct}.
 * @param requestId identificador da requisição, não pode ser nulo ou vazio e deve ser positivo.
 * @param productName nome do produto, não pode ser nulo ou vazio.
 * @param measurementUnit nome da unidade de medida do item, não pode ser nulo ou vazio.
 * @param quantity quantidade solicitada do produto, não pode ser nula ou vazia.
 * @param statusName nome da situação atual do item, não pode ser nulo ou vazio.
 * @param additionalInformations informações adicionais sobre o item.
 */
public record ItemRequestProductRequest(
        @NotNull(message = "O identificador da requisição não deve ser nulo e nem vazio!")
        @Positive(message = "O identificador da requisição deve ser maior ou igual a zero!")
        Long requestId,

        @NotBlank(message = "O nome do produto não deve ser nulo e nem vazio!")
        String productName,

        @NotBlank(message = "A unidade de medida não deve ser nula e nem vazia!")
        String measurementUnit,

        @NotNull(message = "A quantidade não deve ser nula e nem vazia!")
        @Positive(message = "A quantidade deve ser maior ou igual a zero!")
        Double quantity,

        @NotBlank(message = "O nome da situação não deve ser nulo e nem vazio!")
        String statusName,

        String additionalInformations
) {
}
