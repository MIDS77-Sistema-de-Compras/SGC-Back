package net.centroweg.gerenciamentocompras.modules.product.domain.exception;

public class VariationNotFoundException extends RuntimeException {

    public VariationNotFoundException(Long id) {
        super("Variação não encontrada com id: " + id);
    }

}