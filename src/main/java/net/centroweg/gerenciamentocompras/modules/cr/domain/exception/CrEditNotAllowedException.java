package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Lançada quando um usuário que não é coordenador do CR Master
 * tenta editar um Centro de Responsabilidade.
 *
 * <p>Resulta em uma resposta HTTP 403 (Forbidden).</p>
 */
public class CrEditNotAllowedException extends BusinessException {
    public CrEditNotAllowedException() {
        super("Apenas o coordenador do CR Master pode editar CRs.", HttpStatus.FORBIDDEN);
    }
}
