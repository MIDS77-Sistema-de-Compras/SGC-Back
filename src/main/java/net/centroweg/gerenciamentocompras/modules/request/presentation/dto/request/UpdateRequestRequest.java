package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;


import jakarta.validation.Valid;

import java.util.List;

public record UpdateRequestRequest(

                Long crBranchId,

                        @Valid
                        List<CreateRequestItemRequest> items

) {}
