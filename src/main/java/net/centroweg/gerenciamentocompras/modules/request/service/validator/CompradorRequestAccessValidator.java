package net.centroweg.gerenciamentocompras.modules.request.service.validator;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotApprovedForCompradorException;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Validador responsável por restringir o acesso do comprador às solicitações.
 *
 * <p>O comprador só pode visualizar ou alterar solicitações já finalizadas pelo
 * supervisor/coordenador com um resultado que gera trabalho de compra: "Aprovado",
 * "Auto-aprovado" ou "Parcialmente aprovada". "Recusado" fica de fora (nada a comprar).
 * A partir da entrada, o comprador segue conduzindo a solicitação por seus próprios
 * status (em atendimento, atrasada, entregue etc.) — por isso esses também precisam
 * estar liberados aqui: essa validação roda a cada chamada com o status ATUAL da
 * solicitação, então travar nos 3 status de entrada impediria o segundo PATCH em diante
 * (ex.: Em atendimento -> Entregue). Usuários de outras roles não são afetados.</p>
 */
@Component
public class CompradorRequestAccessValidator {

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "aprovado",
            "auto_aprovado",
            "parcialmente_aprovada",
            "em atendimento",
            "atrasada",
            "recebimento_parcial",
            "fundo_rotativo",
            "cd_central",
            "solicitado_portal",
            "parcialmente_atendida",
            "entregue",
            "pedido cancelado"
    );

    /**
     * Valida o acesso do usuário autenticado à solicitação informada.
     *
     * @param request solicitação alvo
     * @throws RequestNotApprovedForCompradorException se um comprador tentar
     *         acessar uma solicitação fora do fluxo de compra
     */
    public void validate(Request request) {
        if (!isCompradorAuthenticated()) {
            return;
        }

        String statusName = request.getStatus() != null && request.getStatus().getName() != null
                ? request.getStatus().getName().trim().toLowerCase()
                : "";

        if (!ALLOWED_STATUSES.contains(statusName)) {
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
