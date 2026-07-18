package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;

/**
 * DTO de entrada para criação e atualização de uma {@link MeasurementUnit}.
 * @param name nome da unidade de medida, não deve ser nulo ou vazio e deve respeitar o tamanho definido.
 * @param abbreviation abreviação(sigla) da unidade de medida, não deve ser nula ou vazia e deve respeitar o tamanho definido.
 */
public record MeasurementUnitRequest(

    @NotBlank(message = "O nome da unidade de medida não deve ser nulo e nem vazio!")
    @Size(min = 2, max = 50,
          message = "O nome deve ter entre 2 e 50 caracteres.")
    String name,

    @NotBlank(message = "A abreviação(sigla) não deve ser nula e nem vazia!")
    @Size(min = 1, max = 10,
          message = "A abreviação deve ter entre 1 e 10 caracteres.")
    String abbreviation
) {
}