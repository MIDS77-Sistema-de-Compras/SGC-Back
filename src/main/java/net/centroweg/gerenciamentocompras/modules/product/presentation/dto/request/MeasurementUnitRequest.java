package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/**
 * DTO de requisição para criação e atualização de uma unidade de medida.
 *
 * <p>Utilizado como corpo da requisição ({@code @RequestBody}) nos endpoints
 * {@code POST /measurement-unit} e {@code PUT /measurement-unit/{id}}.
 * Os campos são validados automaticamente pelo Bean Validation
 * antes de chegarem ao serviço.</p>
 *
 * <p>Exemplo de payload JSON:</p>
 * <pre>{@code
 * {
 *   "name": "Quilograma",
 *   "abbreviation": "kg"
 * }
 * }</pre>
 *
 * @param name         nome completo da unidade de medida; não pode ser nulo nem vazio,
 *                     deve conter entre 2 e 50 caracteres
 * @param abbreviation abreviação (sigla) da unidade de medida; não pode ser nula nem vazia,
 *                     deve conter entre 1 e 10 caracteres
 *
 * @author Lucas Schlei
 * @version 1.0
 * @since 1.0
 */
public record MeasurementUnitRequest(
    
    @NotBlank(message = "O nome da unidade de medida é obrigatório.")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres.")
    String name,

    @NotBlank(message = "A abreviação (sigla) é obrigatória.")
    @Size(min = 1, max = 10, message = "A abreviação deve ter entre 1 e 10 caracteres")
    String abbreviation
) {
    
}
