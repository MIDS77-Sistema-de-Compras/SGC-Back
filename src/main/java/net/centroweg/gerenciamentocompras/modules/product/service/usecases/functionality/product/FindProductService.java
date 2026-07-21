package net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.product;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.IProductMapper;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Caso de uso responsável por buscar ou listar um {@link Product} pelo seu identificador ou nome.
 */
@Service
@RequiredArgsConstructor
public class FindProductService {

    private final ProductRepository productRepository;
    private final IProductMapper productMapper;

    /**
     * Busca um produto no banco de dados pelo ID informado.
     * @param id identificador do produto.
     * @return produto encontrado, caso exista.
     * @throws ProductNotFoundException caso nenhum produto seja encontrado.
     */
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponse(product);
    }

    /**
     * Lista todos os produtos cadastrados no banco de dados.
     * @return lista com todos os produtos encontrados, caso exista.
     */
    public List<ProductResponse> findAll() {
        return productMapper.toResponseList(productRepository.findAll());
    }

    /**
     * Busca um produto no banco de dados pelo nome informado.
     * @param name nome do produto.
     * @return lista com todos os produtos encontrados, caso exista.
     */
    public List<ProductResponse> findByName(String name) {
        return productMapper.toResponseList(
                productRepository.findByNameContainingIgnoreCase(name)
        );
    }

}