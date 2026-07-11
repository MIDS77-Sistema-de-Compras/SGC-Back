package net.centroweg.gerenciamentocompras.modules.request.service.api;

import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;

import java.util.Optional;

public interface RequestPublicApi {

    Optional<Product> findProuctByNameIgnoreCase(String name);
    Optional<MeasurementUnit> findMeasurementByNameIgnoreCase(String name);
}
