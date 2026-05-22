package net.centroweg.gerenciamentocompras.modules.product.domain.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Produto não encontrado com id: " + id);
    }

}