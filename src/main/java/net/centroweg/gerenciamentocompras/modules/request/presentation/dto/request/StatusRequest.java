package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/**
 * DTO responsável por receber os dados de criação
 * e atualização de um status.
 *
 * <p>Contém validações para garantir a integridade
 * das informações recebidas na requisição.</p>
 *
 * @param name nome do status
 * @param description descrição do status
 *
 * @author André
 * @since 1.0
 */
public record StatusRequest(

        @NotBlank(message = "O nome não pode ser nulo.")
        @Size(min = 2, max = 25, message = "O nome deve ter entre 2 e 25 caracteres.")
        String name,

        @NotBlank(message = "O status deve conter uma descrição.")
        @Size(min = 10, max = 100, message = "A descrição deve conter entre 10 e 100 caracteres.")
        String description

) {
}
