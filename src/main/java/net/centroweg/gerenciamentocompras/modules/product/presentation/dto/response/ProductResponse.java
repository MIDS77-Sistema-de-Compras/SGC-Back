
package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response;


public record ProductResponse(

            Long id,
                String name,
                String description,
                Double price,
                String type,
                String code

) {}

