package net.centroweg.gerenciamentocompras.modules.request.service.api;

import java.util.Optional;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;

public interface RequestPublicApi {

    Optional<Product> findProuctByNameIgnoreCase(String name);
    Optional<MeasurementUnit> findMeasurementByNameIgnoreCase(String nameOrAbbreviation);
    Product createProduct(CreateProductRequest request);
}