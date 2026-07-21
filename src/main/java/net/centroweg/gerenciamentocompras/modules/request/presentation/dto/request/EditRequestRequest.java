package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record EditRequestRequest(
        @NotNull(message = "A filial/CR é obrigatória.")
        Long crBranchId,

        @Valid
        List<RequestProductItemRequest> products,

        @Valid
        List<RequestProvisionItemRequest> provisions,

        List<Long> retainedAttachmentIds
) {

    @AssertTrue(message = "Informe produtos ou serviços, nunca os dois ao mesmo tempo.")
    public boolean isOnlyOneItemTypeProvided() {
        return !hasItems(products) || !hasItems(provisions);
    }

    @AssertTrue(message = "A solicitação deve conter ao menos um produto ou serviço.")
    public boolean isAnyItemProvided() {
        return hasItems(products) || hasItems(provisions);
    }

    private boolean hasItems(List<?> items) {
        return items != null && !items.isEmpty();
    }
}
