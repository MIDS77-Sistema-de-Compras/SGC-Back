package net.centroweg.gerenciamentocompras.modules.request.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * A classe responsável por implementar os métodos da interface {@link RequestPublicApi}
 */
@Repository
@RequiredArgsConstructor
public class RequestPublicApiImpl implements RequestPublicApi{

    private final ProductRepository productRepository;
    private final MeasurementUnitRepository measurementUnitRepository;

    /**
     * Busca um produto pelo nome, ignorando maiúsculas e minúsculas.
     * @param name nome do produto.
     * @return o produto encontrado, caso exista.
     */
    @Override
    public Optional<Product> findProuctByNameIgnoreCase(String name) {
        return productRepository.findByNameIgnoreCase(name);
    }

    /**
     * Busca uma unidade de medida pelo nome, ignorando maiúsculas e minúsculas.
     * @param name nome da unidade de medida.
     * @return a unidade de medida encontrada, caso exista.
     */
    @Override
    public Optional<MeasurementUnit> findMeasurementByNameIgnoreCase(String name){
        return measurementUnitRepository.findByNameIgnoreCase(name);
    }
}
