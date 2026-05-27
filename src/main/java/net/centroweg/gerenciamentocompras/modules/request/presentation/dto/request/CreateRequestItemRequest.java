package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateRequestItemRequest(

                @NotNull(message = "O produto é obrigatório.")
                        Long productId,

                        @NotNull(message = "A unidade de medida é obrigatória.")
                      Long measurementUnitId,

                        @NotNull(message = "A quantidade é obrigatória.")
                        @Positive(message = "A quantidade deve ser maior que zero.")
                         Double quantity,

                        @Size(max = 255, message = "As informações adicionais devem ter no máximo 255 caracteres.")
                        String additionalInformation

) {}

