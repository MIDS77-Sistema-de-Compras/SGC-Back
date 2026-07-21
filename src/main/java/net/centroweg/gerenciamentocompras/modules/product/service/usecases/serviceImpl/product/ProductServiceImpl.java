package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl.product;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.product.CreateProductService;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.product.DeleteProductService;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.product.FindProductService;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.product.UpdateProductService;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.product.IProductService;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;

/**
 * Classe de serviço do {@link Product} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link IProductService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    /**
     * Componente responsável pela criação de um produto.
     */
    private final CreateProductService createProductService;

    /**
     * Componente responsável por qualquer tipo de busca de produto.
     */
    private final FindProductService findProductService;

    /**
     * Componente responsável pela atualização de um produto.
     */
    private final UpdateProductService updateProductService;

    /**
     * Componente responsável por remover um produto.
     */
    private final DeleteProductService deleteProductService;

    /**
     * Cria e persiste um novo produto no banco de dados.
     * @param request dados do produto.
     * @return produto criado.
     */
    @Override
    public ProductResponse create(CreateProductRequest request) {
        return createProductService.execute(request);
    }

    /**
     * Busca um produto no banco de dados pelo ID informado.
     * @param id identificador do produto.
     * @return produto encontrado, caso exista.
     */
    @Override
    public ProductResponse findById(Long id) {
        return findProductService.findById(id);
    }

    /**
     * Lista todos os produtos cadastrados pelo nome informado.
     * @param name nome do produto
     * @return lista com todos os produtos encontrados, caso exista.
     */
    @Override
    public List<ProductResponse> findAll(String name) {
        if (name != null) {
            return findProductService.findByName(name);
        }
        return findProductService.findAll();
    }

    /**
     * Atualiza um produto existente no banco de dados.
     * @param id identificador do produto.
     * @param request novos dados do produto.
     * @return produto já atualizado.
     */
    @Override
    public ProductResponse update(Long id, UpdateProductRequest request) {
        return updateProductService.execute(id, request);
    }

    /**
     * Deleta um produto do banco de dados.
     * @param id identificador do produto.
     */
    @Override
    public void delete(Long id) {
        deleteProductService.execute(id);
    }

}