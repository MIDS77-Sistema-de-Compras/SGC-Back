package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateRequestRequest(

                @NotNull(message = "A filial/CR é obrigatória.")
                        Long crBranchId,

                        @NotNull(message = "A solicitação deve conter ao menos um item.")
                        @Valid
                        List<CreateRequestItemRequest> items

) {}
