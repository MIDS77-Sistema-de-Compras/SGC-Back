package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import java.time.LocalDate;

public record RequestFilterRequest(
        String crCode,
        String statusName,
        String supervisorName,
        LocalDate startDate,
        LocalDate endDate
) {
}

