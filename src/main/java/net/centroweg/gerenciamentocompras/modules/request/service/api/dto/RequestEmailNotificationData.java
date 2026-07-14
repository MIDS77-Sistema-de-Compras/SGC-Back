package net.centroweg.gerenciamentocompras.modules.request.service.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RequestEmailNotificationData(
        Long requestId,
        String crName,
        String crCode,
        String branchName,
        String statusName,
        String requesterName,
        LocalDateTime requestDate,
        List<RequestProductEmailData> productItems,
        List<RequestProvisionEmailData> provisionItems
) {
    public RequestEmailNotificationData {
        productItems = productItems == null ? List.of() : List.copyOf(productItems);
        provisionItems = provisionItems == null ? List.of() : List.copyOf(provisionItems);
    }
}
