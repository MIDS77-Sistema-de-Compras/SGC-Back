package net.centroweg.gerenciamentocompras.shared.exception;


import org.apache.hc.core5.http.HttpStatus;

public class InvalidFileTypeException extends BusinessException{
    public  InvalidFileTypeException (){
        super("Formato inválido, envie uma imagem do formato png, webp ou jpeg", org.springframework.http.HttpStatus.valueOf(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE));
    }
}
