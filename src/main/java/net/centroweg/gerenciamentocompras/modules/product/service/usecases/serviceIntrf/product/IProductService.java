package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.product;

import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;

/**
 * Interface de serviço para operações de gerenciamento de {@link Product}.
 */
public interface IProductService {

    /**
     * Cria um novo produto.
     * @param request dados do produto.
     * @return produto criado.
     */
    ProductResponse create(CreateProductRequest request);

    /**
     * Busca um produto pelo ID.
     * @param id identificador do produto.
     * @return produto encontrado.
     */
    ProductResponse findById(Long id);

    /**
     * Lista todos os produtos cadastrados pelo nome.
     * @param name nome do produto.
     * @return lista com todos os produtos encontrados.
     */
    List<ProductResponse> findAll(String name);

    /**
     * Atualiza um produto existente.
     * @param id identificador do produto.
     * @param request novos dados do produto.
     * @return produto já atualizado.
     */
    ProductResponse update(Long id, UpdateProductRequest request);

    /**
     * Deleta um produto.
     * @param id identificador do produto.
     */
    void delete(Long id);

}