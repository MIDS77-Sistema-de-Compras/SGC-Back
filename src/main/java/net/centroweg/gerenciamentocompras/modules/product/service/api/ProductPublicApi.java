package net.centroweg.gerenciamentocompras.modules.product.service.api;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;

import java.util.Optional;

/**
 * Fachada de comunicação entre módulos para o módulo {@code product}.
 *
 * <p>Expõe apenas o necessário para que outros módulos consultem e criem
 * produtos/unidades de medida sem acessar os repositórios internos
 * diretamente, respeitando o isolamento do monólito modular.</p>
 */
public interface ProductPublicApi {

    /**
     * Busca um produto pelo nome exato, ignorando maiúsculas/minúsculas.
     *
     * @param name nome do produto
     * @return um {@link Optional} com o produto, caso exista
     */
    Optional<Product> findByNameIgnoreCase(String name);

    /**
     * Busca uma unidade de medida pelo nome ou pela abreviação,
     * ignorando maiúsculas/minúsculas.
     *
     * @param nameOrAbbreviation nome ou abreviação da unidade de medida
     * @return um {@link Optional} com a unidade de medida, caso exista
     */
    Optional<MeasurementUnit> findMeasurementByNameOrAbbreviation(String nameOrAbbreviation);

    /**
     * Cria um novo produto e retorna a entidade persistida.
     *
     * @param request dados do produto a ser criado
     * @return o {@link Product} criado
     */
    Product createProduct(CreateProductRequest request);

}
