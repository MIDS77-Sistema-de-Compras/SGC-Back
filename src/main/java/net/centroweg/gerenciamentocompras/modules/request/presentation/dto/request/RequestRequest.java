package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * DTO de entrada para criação de uma {@link Request}.
 * @param crBranchId identificador da filial/CR, não pode ser nulo.
 * @param userIds lista de identificadores dos usuários atribuídos à solicitação, no máximo 2 além do solicitante.
 */
public record RequestRequest(
        @NotNull(message = "A filial/CR é obrigatória.")
        Long crBranchId,

        @Size(max = 2, message = "Apenas 3 usuários incluindo você podem ser atribuídos a uma solicitação")
        List<Long> userIds
) {}
