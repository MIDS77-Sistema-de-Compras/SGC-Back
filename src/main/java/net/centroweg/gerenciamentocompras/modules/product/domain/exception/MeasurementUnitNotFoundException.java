package net.centroweg.gerenciamentocompras.modules.product.domain.exception;

import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;

public class MeasurementUnitNotFoundException extends BusinessException {
    
    public MeasurementUnitNotFoundException() {
        super("Unidade de medida não encontrada.", HttpStatus.NOT_FOUND);
    }

   //to customize the message dynamically somewhere
    public MeasurementUnitNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}