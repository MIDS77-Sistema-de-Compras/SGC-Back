package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateApprovalDelegationRequest(
        @NotNull(message = "O substituto deve ser informado.")
        Long delegateUserId,

        @NotNull(message = "A data de início deve ser informada.")
        @Schema(example = "2026-08-01T08:00:00", description = "Data local ISO-8601 no fuso padrão da aplicação")
        LocalDateTime startAt,

        @NotNull(message = "A data de término deve ser informada.")
        @Schema(example = "2026-08-15T18:00:00", description = "Data local ISO-8601 no fuso padrão da aplicação")
        LocalDateTime endAt
) {
}
