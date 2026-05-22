package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request;

    public record UpdateProductRequest(

            String name,

            String description,

            Double price,

            String type

    ) {}

