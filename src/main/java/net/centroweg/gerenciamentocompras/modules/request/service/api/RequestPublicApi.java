package net.centroweg.gerenciamentocompras.modules.request.service.api;

import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;
import java.util.Optional;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Interface de API responsável por compartilhar métodos específicos do módulo {@link Request}.
 */
public interface RequestPublicApi {

    /**
     * Busca um produto pelo nome, ignorando maiúsculas e minúsculas.
     * @param name nome do produto.
     * @return o produto encontrado, caso exista.
     */
    Optional<Product> findProuctByNameIgnoreCase(String name);

    /**
     * Busca uma unidade de medida pelo nome, ignorando maiúsculas e minúsculas.
     * @param name nome da unidade de medida.
     * @return a unidade de medida encontrada, caso exista.
     */
    Optional<MeasurementUnit> findMeasurementByNameIgnoreCase(String name);
}
