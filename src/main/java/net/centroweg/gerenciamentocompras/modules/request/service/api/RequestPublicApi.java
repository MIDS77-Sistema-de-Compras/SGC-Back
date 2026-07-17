package net.centroweg.gerenciamentocompras.modules.request.service.api;

import java.util.Optional;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestStatusNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestEmailNotificationData;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RequestPublicApi {

    Optional<Product> findProuctByNameIgnoreCase(String name);
    Optional<MeasurementUnit> findMeasurementByNameIgnoreCase(String nameOrAbbreviation);
    Product createProduct(CreateProductRequest request);
    RequestNotificationData findNotificationDataById(Long requestId);
    RequestStatusNotificationData findStatusNotificationDataById(Long requestId);
    RequestEmailNotificationData findEmailNotificationDataById(Long requestId);
    Optional<Request> findRequestById(Long requestId);
    List<ItemRequestProduct> findItemProductsByIds(Collection<Long> ids);
    List<ItemRequestProvision> findItemProvisionsByIds(Collection<Long> ids);
}
