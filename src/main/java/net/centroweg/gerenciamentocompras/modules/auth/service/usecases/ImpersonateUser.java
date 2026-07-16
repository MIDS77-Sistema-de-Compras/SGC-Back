package net.centroweg.gerenciamentocompras.modules.auth.service.usecases;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.domain.exception.ImpersonationNotAllowedException;
import net.centroweg.gerenciamentocompras.modules.auth.presentation.dto.response.ImpersonationStatusResponse;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.modules.auth.service.api.AuthPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela impersonação de usuários pelo administrador.
 *
 * <p>Permite que um administrador entre na conta de outro usuário (assumindo
 * as permissões dele), consulte o estado da impersonação e retorne à própria
 * conta. As ações executadas durante a impersonação são auditadas com uma
 * descrição indicando o administrador responsável.</p>
 */
@Service
@RequiredArgsConstructor
public class ImpersonateUser {

    private final AuthPublicApi authPublicApi;
    private final JwtService jwtService;
    private final CurrentUserService currentUserService;

    /**
     * Gera um token de impersonação para a conta do usuário informado.
     *
     * @param userId identificador do usuário-alvo
     * @return token JWT da conta do usuário, com as claims de impersonação
     * @throws ImpersonationNotAllowedException se o alvo for o próprio admin,
     *         um usuário inativo ou outro administrador
     */
    public String impersonate(Long userId) {
        User admin = currentUserService.getCurrentUser();

        User target = authPublicApi.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o ID: " + userId));

        validateTarget(admin, target);

        return jwtService.generateImpersonationToken(new UserPrincipal(target), admin.getEmail(), admin.getName());
    }

    /**
     * Encerra a impersonação e devolve um token da conta original do administrador.
     *
     * @param currentToken token JWT atual (de impersonação)
     * @return token JWT do administrador original
     * @throws ImpersonationNotAllowedException se o token atual não for de impersonação
     */
    public String stopImpersonation(String currentToken) {
        Claims claims = currentToken != null ? jwtService.parseClaims(currentToken) : null;
        String adminEmail = claims != null ? claims.get("impersonatedBy", String.class) : null;

        if (adminEmail == null) {
            throw new ImpersonationNotAllowedException("Você não está logado na conta de outro usuário.");
        }

        User admin = authPublicApi.findByEmailOrCpf(adminEmail, adminEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Administrador original não encontrado."));

        if (!Boolean.TRUE.equals(admin.getActive()) || !hasRole(admin, Authorities.ADMIN)) {
            throw new ImpersonationNotAllowedException("A conta do administrador original não está mais disponível.");
        }

        return jwtService.generateToken(new UserPrincipal(admin));
    }

    /**
     * Informa se o token atual é de impersonação e quem são os envolvidos.
     *
     * @param currentToken token JWT atual (pode ser nulo)
     * @return estado da impersonação
     */
    public ImpersonationStatusResponse status(String currentToken) {
        Claims claims = currentToken != null ? jwtService.parseClaims(currentToken) : null;
        String adminName = claims != null ? claims.get("impersonatedByName", String.class) : null;

        if (adminName == null) {
            return new ImpersonationStatusResponse(false, null, null);
        }

        return new ImpersonationStatusResponse(true, adminName, claims.get("nome", String.class));
    }

    private void validateTarget(User admin, User target) {
        if (target.getId().equals(admin.getId())) {
            throw new ImpersonationNotAllowedException("Você já está logado nesta conta.");
        }

        if (!Boolean.TRUE.equals(target.getActive())) {
            throw new ImpersonationNotAllowedException("Não é possível logar na conta de um usuário inativo.");
        }

        if (hasRole(target, Authorities.ADMIN)) {
            throw new ImpersonationNotAllowedException("Não é possível logar na conta de outro administrador.");
        }
    }

    private boolean hasRole(User user, String roleName) {
        return user.getRole() != null
                && user.getRole().getName() != null
                && user.getRole().getName().trim().equalsIgnoreCase(roleName);
    }
}
