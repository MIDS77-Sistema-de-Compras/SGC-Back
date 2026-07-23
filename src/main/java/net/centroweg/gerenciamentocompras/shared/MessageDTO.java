package net.centroweg.gerenciamentocompras.shared;

/**
 * DTO de saída com uma mensagem de retorno da API.
 * @param text conteúdo da mensagem.
 */
public record MessageDTO(
        String text
) {
}
