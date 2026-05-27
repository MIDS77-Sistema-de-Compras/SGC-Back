package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateProductRequest(

            @NotBlank
            String name,

            String description,

            @NotNull
            @Positive
            Double price,

            String type,

            @NotBlank
            String code,

            @NotBlank
            String variation

) {}



