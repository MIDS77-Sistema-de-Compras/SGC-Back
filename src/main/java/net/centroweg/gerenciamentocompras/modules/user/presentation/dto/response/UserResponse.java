package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response;

import java.time.LocalDateTime;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * DTO de saída com os dados de um {@link User}.
 * @param id identificador do usuário.
 * @param name nome do usuário.
 * @param cpf CPF do usuário.
 * @param email endereço de email do usuário.
 * @param extensionNumber ramal para contato interno do usuário.
 * @param active atividade do usuário.
 * @param createdAt data e hora de criação do usuário.
 * @param updatedAt data e hora da última atualização do usuário.
 * @param userProfile foto de perfil do usuário.
 */
public record UserResponse(
        Long id,

        String name,

        String cpf,

        String email,

        String extensionNumber,

        Boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        String userProfile
) {
}
