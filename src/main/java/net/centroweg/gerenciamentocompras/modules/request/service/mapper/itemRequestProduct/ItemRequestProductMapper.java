package net.centroweg.gerenciamentocompras.modules.request.service.mapper.itemRequestProduct;

import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pela conversão entre a entidade({@link ItemRequestProduct}) e seus DTOs de entrada({@link ItemRequestProductRequest}) e saída({@link ItemRequestProductResponse}).
 */
@Component
public class ItemRequestProductMapper {

    /**
     * Converte uma entidade item de produto da solicitação em um DTO de saída do item de produto da solicitação.
     * @param item entidade com os dados do item de produto da solicitação.
     * @return dados convertidos para DTO de saída.
     */
    public ItemRequestProductResponse toResponse(ItemRequestProduct item) {
        return new ItemRequestProductResponse(
                item.getId(),
                item.getRequest().getId(),
                item.getProduct() != null ? item.getProduct().getName() : null,
                item.getMeasurementUnit() != null ? item.getMeasurementUnit().getName() : null,
                item.getQuantity(),
                item.getStatusId() != null ? item.getStatusId().getName() : null,
                item.getAdditionalInformations()
        );
    }

    /**
     * Converte um DTO de entrada do item de produto da solicitação em uma entidade item de produto da solicitação.
     * @param dto dados do item de produto da solicitação.
     * @param request dados da solicitação.
     * @param product dados do produto.
     * @param measurementUnit dados da unidade de medida.
     * @param status dados do status.
     * @return dados convertidos para entidade.
     */
    public ItemRequestProduct toEntity(
            ItemRequestProductRequest dto,
            Request request,
            Product product,
            MeasurementUnit measurementUnit,
            Status status
    ) {

        ItemRequestProduct item = new ItemRequestProduct();

        item.setRequest(request);
        item.setProduct(product);
        item.setMeasurementUnit(measurementUnit);
        item.setQuantity(dto.quantity());
        item.setStatusId(status);
        item.setAdditionalInformations(dto.additionalInformations());
        return item;
    }
}
