package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * DTO de saída com o resumo dos dados de uma {@link Request}.
 * @param name nome do solicitante.
 * @param cpf CPF do solicitante.
 * @param email e-mail do solicitante.
 * @param extensionNumber ramal do solicitante.
 */
public record RequestResumeDTO (
        String name,
        String cpf,
        String email,
        String extensionNumber
){
}
