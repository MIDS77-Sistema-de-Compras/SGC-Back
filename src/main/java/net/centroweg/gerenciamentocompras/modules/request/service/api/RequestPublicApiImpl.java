package net.centroweg.gerenciamentocompras.modules.request.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.CreateProductService;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RequestPublicApiImpl implements RequestPublicApi {

    private final ProductRepository productRepository;
    private final MeasurementUnitRepository measurementUnitRepository;
    private final CreateProductService createProductService;

    @Override
    public Optional<Product> findProuctByNameIgnoreCase(String name) {
        return productRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Optional<MeasurementUnit> findMeasurementByNameIgnoreCase(String nameOrAbbreviation) {
        return measurementUnitRepository.findMeasurementByNameIgnoreCaseOrAbbreviationIgnoreCase(
                nameOrAbbreviation,
                nameOrAbbreviation
        );
    }

    @Override
    public Product createProduct(CreateProductRequest request) {
        ProductResponse response = createProductService.execute(request);
        return productRepository.findById(response.id())
                .orElseThrow(ProductNotFoundException::new);
    }
}