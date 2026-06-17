package net.centroweg.gerenciamentocompras.modules.request.service.mapper.itemRequestProduct;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ItemRequestProductMapper {

    public ItemRequestProductResponse toResponse(ItemRequestProduct item) {
        return new ItemRequestProductResponse(
                item.getId(),
                item.getRequest().getId(),
                item.getProduct() != null ? item.getProduct().getName() : null,
                item.getMeasurementUnit() != null ? item.getMeasurementUnit().getName() : null,
                item.getQuantity(),
                item.getStatus_id() != null ? item.getStatus_id().getName() : null,
                item.getAdditionalInformations()
        );
    }

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
        item.setStatus_id(status);
        item.setAdditionalInformations(dto.additionalInformations());
        return item;
    }
}
