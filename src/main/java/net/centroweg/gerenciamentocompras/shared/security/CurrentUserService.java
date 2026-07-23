package net.centroweg.gerenciamentocompras.shared.security;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Classe de serviço responsável por obter o {@link User} autenticado no contexto de segurança.
 */
@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    /**
     * Busca no banco de dados o usuário autenticado no contexto de segurança.
     * @return usuário autenticado, caso exista.
     * @throws AcessDeniedException caso não haja usuário autenticado ou nenhum usuário seja encontrado.
     */
    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AcessDeniedException();
        }

        String username = authentication.getName();

        return userRepository.findByEmailOrCpf(username, username)
                .orElseThrow(AcessDeniedException::new);
    }
}