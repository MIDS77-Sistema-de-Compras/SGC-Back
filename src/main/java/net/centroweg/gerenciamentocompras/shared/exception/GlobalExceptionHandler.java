package net.centroweg.gerenciamentocompras.shared.exception;

import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.AuthenticationException;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;

/**
 * Componente responsável pelo tratamento global das exceções lançadas pela aplicação.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Monta a resposta de erro da API com os dados informados.
     * @param httpStatus código de status HTTP do erro.
     * @param message mensagem descritiva do erro.
     * @param errors mapa com os erros específicos de cada campo.
     * @return resposta de erro já montada.
     */
    private ResponseEntity<ApiError> buildResponse(HttpStatus httpStatus, String message, Map<String, String> errors) {
        return ResponseEntity.status(httpStatus)
                .body(new ApiError(httpStatus.value(), message, errors));
    }

    /**
     * Trata os erros de validação dos argumentos informados na requisição.
     * @param exception exceção lançada.
     * @return resposta de erro com os campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(
                        error.getField(),
                        error.getDefaultMessage()
                ));

        return buildResponse(HttpStatus.BAD_REQUEST, "Dados inválidos: verifique os campos obrigatórios.", errors);
    }

    /**
     * Trata os erros de violação das restrições de validação.
     * @param exception exceção lançada.
     * @return resposta de erro correspondente.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Erro de validação: verifique os dados obrigatórios.", null);
    }

    /**
     * Trata os erros de violação da integridade dos dados.
     * @param exception exceção lançada.
     * @return resposta de erro correspondente.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException exception) {
        return buildResponse(HttpStatus.CONFLICT, "Conflito de dados: este registro já existe ou possui dependências.", null);
    }

    /**
     * Trata os erros de falha no acesso ao recurso de dados.
     * @param exception exceção lançada.
     * @return resposta de erro correspondente.
     */
    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ApiError> handleDataAccessResource(DataAccessResourceFailureException exception) {
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, "Serviço temporariamente indisponível. Tente novamente mais tarde.", null);
    }

    /**
     * Trata os erros das regras de negócio da aplicação.
     * @param exception exceção lançada.
     * @return resposta de erro correspondente.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException exception) {
        return buildResponse(exception.getHttpStatus(), exception.getMessage(), null);
    }

    /**
     * Trata os erros de autenticação do usuário.
     * @param exception exceção lançada.
     * @return resposta de erro correspondente.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(AuthenticationException exception) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Credenciais inválidas: verifique seu usuário e senha.", null);
    }

    /**
     * Trata os erros inesperados não mapeados pelos demais tratadores.
     * @param exception exceção lançada.
     * @return resposta de erro correspondente.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception exception) {
        log.error("Unexpected error: ", exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado.", null);
    }

    /**
     * Trata os erros de CR não encontrado.
     * @param exception exceção lançada.
     * @return resposta de erro correspondente.
     */
    @ExceptionHandler(CrNotFoundException.class)
    public ResponseEntity<ApiError> handleCrNotFound(CrNotFoundException exception){
        return  buildResponse(exception.getHttpStatus(), exception.getMessage(), null);
    }

    /**
     * Trata os erros de provisão não encontrada.
     * @param exception exceção lançada.
     * @return resposta de erro correspondente.
     */
    @ExceptionHandler(ProvisionNotFoundException.class)
    public ResponseEntity<ApiError> handleProvisionNotFound(ProvisionNotFoundException exception){
        return buildResponse(exception.getHttpStatus(), exception.getMessage(), null);
    }

    /**
     * Trata os erros de filial não encontrada.
     * @param exception exceção lançada.
     * @return resposta de erro correspondente.
     */
    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<ApiError> handleBranchNotFound(BranchNotFoundException exception) {
        return buildResponse(exception.getHttpStatus(), exception.getMessage(), null);
    }

    /**
     * Trata os erros ocorridos durante a montagem ou o envio de emails.
     * @param exception exceção lançada.
     * @return resposta de erro correspondente.
     */
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ApiError> handleMessagingException(MessagingException exception){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), null);
    }

}
