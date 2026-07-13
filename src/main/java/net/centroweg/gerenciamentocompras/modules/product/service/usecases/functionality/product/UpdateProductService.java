package net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.product;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.IProductMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela atualização de um {@link Product}.
 */
@Service
@RequiredArgsConstructor
public class UpdateProductService {

    private final ProductRepository productRepository;
    private final IProductMapper productMapper;

    /**
     * Atualiza um produto existente.
     * @param id identificador do produto.
     * @param request novos dados do produto.
     * @return produto já atualizado.
     */
    public ProductResponse execute(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setType(request.type());
        product.setCode(request.code());
        return productMapper.toResponse(productRepository.save(product));
    }

}
