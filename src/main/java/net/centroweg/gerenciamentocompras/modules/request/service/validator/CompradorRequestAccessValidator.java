package net.centroweg.gerenciamentocompras.modules.request.service.validator;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotApprovedForCompradorException;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Validador responsável por restringir o acesso do comprador às solicitações.
 *
 * <p>O comprador só pode visualizar ou alterar solicitações com status
 * "Aprovado". Usuários de outras roles não são afetados.</p>
 */
@Component
public class CompradorRequestAccessValidator {

    private static final String APPROVED_STATUS = "aprovado";

    /**
     * Valida o acesso do usuário autenticado à solicitação informada.
     *
     * @param request solicitação alvo
     * @throws RequestNotApprovedForCompradorException se um comprador tentar
     *         acessar uma solicitação cujo status não é "Aprovado"
     */
    public void validate(Request request) {
        if (!isCompradorAuthenticated()) {
            return;
        }

        String statusName = request.getStatus() != null && request.getStatus().getName() != null
                ? request.getStatus().getName().trim().toLowerCase()
                : "";

        if (!APPROVED_STATUS.equals(statusName)) {
            throw new RequestNotApprovedForCompradorException();
        }
    }

    private boolean isCompradorAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities() != null
                && authentication.getAuthorities().stream()
                .anyMatch(authority -> Authorities.COMPRADOR.equals(authority.getAuthority()));
    }
}
