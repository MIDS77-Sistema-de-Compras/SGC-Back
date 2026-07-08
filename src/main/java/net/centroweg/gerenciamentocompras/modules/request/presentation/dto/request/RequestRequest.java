package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RequestRequest(

        @NotNull(message = "A filial/CR e obrigatoria.")
        Long crBranchId,

        @Size(max = 2, message = "Apenas 3 usuarios incluindo voce pode ser atribuido a uma solicitacao")
        List<Long> userIds,

        @Valid
        List<RequestProductItemRequest> products,

        @Valid
        List<RequestProvisionItemRequest> provisions
) {

    @AssertTrue(message = "Informe produtos ou servicos, nunca os dois ao mesmo tempo.")
    public boolean isOnlyOneItemTypeProvided() {
        return !hasItems(products) || !hasItems(provisions);
    }

    @AssertTrue(message = "A solicitacao deve conter ao menos um produto ou servico.")
    public boolean isAnyItemProvided() {
        return hasItems(products) || hasItems(provisions);
    }

    private boolean hasItems(List<?> items) {
        return items != null && !items.isEmpty();
    }
}