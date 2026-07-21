package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.product;

import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;

/**
 * Interface de serviço para operações de gerenciamento do {@link Product}.
 */
public interface IProductService {

    /**
     * Cria e persiste um novo produto no banco de dados.
     * @param request dados do produto.
     * @return produto criado.
     */
    ProductResponse create(CreateProductRequest request);

    /**
     * Busca um produto no banco de dados pelo ID informado.
     * @param id identificador do produto.
     * @return produto encontrado, caso exista.
     */
    ProductResponse findById(Long id);

    /**
     * Lista todos os produtos cadastrados no banco de dados pelo nome.
     * @param name nome do produto.
     * @return lista com todos os produtos encontrados, caso exista.
     */
    List<ProductResponse> findAll(String name);

    /**
     * Atualiza um produto existente no banco de dados.
     * @param id identificador do produto.
     * @param request novos dados do produto.
     * @return produto já atualizado.
     */
    ProductResponse update(Long id, UpdateProductRequest request);

    /**
     * Deleta um produto do banco de dados.
     * @param id identificador do produto.
     */
    void delete(Long id);

}