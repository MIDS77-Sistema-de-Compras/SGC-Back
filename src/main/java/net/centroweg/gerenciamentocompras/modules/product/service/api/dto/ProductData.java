package net.centroweg.gerenciamentocompras.modules.product.service.api.dto;

    public record ProductData(

            Long id,
            String name,
            String code,
            Double price

    ) {}

