package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Dados de entrada para criar ou atualizar um vínculo entre CR e filial.
 *
 * @param branchId identificador da filial (obrigatório)
 * @param crId identificador do Centro de Responsabilidade (obrigatório)
 * @param responsibleUsersId identificador do usuário responsável (opcional)
 */
public record CrBranchRequest(
        @NotNull Long branchId,
        @NotNull Long crId,
        List<Long> responsibleUsersId
) {
}