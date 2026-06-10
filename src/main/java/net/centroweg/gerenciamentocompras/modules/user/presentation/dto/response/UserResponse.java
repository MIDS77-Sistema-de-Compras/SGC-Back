package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String cpf,
        String email,
        String extensionNumber,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String userProfile
) {
}
