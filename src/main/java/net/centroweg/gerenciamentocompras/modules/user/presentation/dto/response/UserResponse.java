package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response;

import java.time.LocalDateTime;

/**
 * DTO de resposta para métodos de CRUD do usuário
 */
public record UserResponse(
        /**
         * Id do usuário
         */
        Long id,
        /**
         * Nome do usuário
         */
        String name,
        /**
         * CPF do usuário
         */
        String cpf,
        /**
         * E-mail do usuário
         */
        String email,
        /**
         * Ramal para contato interno do usuário
         */
        String extensionNumber,
        /**
         * Atividade do usuário
         */
        Boolean active,
        /**
         * Data de criação do usuário
         */
        LocalDateTime createdAt,
        /**
         * Data de atualização do usuário
         */
        LocalDateTime updatedAt,
        String userProfile

) {
}
