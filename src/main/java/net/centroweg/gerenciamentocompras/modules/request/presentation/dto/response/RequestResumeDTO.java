package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

public record RequestResumeDTO (
        String name,
        String cpf,
        String email,
        String extensionNumber
){
}
