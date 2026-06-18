package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AttachmentUploadException extends BusinessException {

    public AttachmentUploadException() {
        super(
                "Não foi possível enviar o arquivo.",
                HttpStatus.BAD_GATEWAY
        );
    }
}