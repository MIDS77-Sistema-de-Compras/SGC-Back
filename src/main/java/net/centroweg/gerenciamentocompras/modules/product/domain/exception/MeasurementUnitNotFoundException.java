package net.centroweg.gerenciamentocompras.modules.product.domain.exception;

import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;

/**
 * Exceção lançada quando uma unidade de medida
 * não é encontrada no sistema.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */
public class MeasurementUnitNotFoundException extends BusinessException {

    /**
     * Cria uma exceção com a mensagem padrão
     * de unidade de medida não encontrada.
     */
    public MeasurementUnitNotFoundException() {
        super("Unidade de medida não encontrada.", HttpStatus.NOT_FOUND);
    }

    /**
     * Cria uma exceção com mensagem personalizada.
     *
     * @param message Mensagem detalhando o erro ocorrido.
     */
    public MeasurementUnitNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}