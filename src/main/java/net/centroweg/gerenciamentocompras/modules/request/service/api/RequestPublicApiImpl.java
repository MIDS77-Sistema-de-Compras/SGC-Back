package net.centroweg.gerenciamentocompras.modules.request.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RequestPublicApiImpl implements RequestPublicApi{

    private final ProductRepository productRepository;
    private final MeasurementUnitRepository measurementUnitRepository;


    @Override
    public Optional<Product> findProuctByNameIgnoreCase(String name) {
        return productRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Optional<MeasurementUnit> findMeasurementByNameIgnoreCase(String name){
        return measurementUnitRepository.findByNameIgnoreCase(name);
    }
}
