package net.centroweg.gerenciamentocompras.modules.product.domain.exception;

import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;

/**
 * Exceção lançada quando uma {@link MeasurementUnit} não é encontrada no banco de dados.
 */
public class MeasurementUnitNotFoundException extends BusinessException {

    public MeasurementUnitNotFoundException() {
        super("Unidade de medida não encontrada.", HttpStatus.NOT_FOUND);
    }

    public MeasurementUnitNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}