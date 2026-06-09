package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateProductRequest(

            @NotBlank
            String name,

            String description,

            @NotNull
            Double price,

            @NotBlank
            String type,

            @NotBlank
            String code

) {}