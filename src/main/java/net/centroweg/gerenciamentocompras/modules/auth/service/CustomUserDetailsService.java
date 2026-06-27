package net.centroweg.gerenciamentocompras.modules.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.config.security.CpfHasher;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.service.api.AuthPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Implementação do {@link UserDetailsService} responsável por carregar os dados do usuário durante o processo de autenticação.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * API responsável pela consulta de usuários.
     */
    private final AuthPublicApi authPublicApi;

    /**
     * Componente responsável por gerar o hash do CPF utilizado nas consultas.
     */
    private final CpfHasher cpfHasher;

    /**
     * Carrega os dados do usuário a partir do login informado.
     *  @param login e-mail ou CPF informado pelo usuário.
     * @return objeto contendo os dados do usuário autenticado.
     * @throws UsernameNotFoundException caso nenhum usuário seja encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        String cleanLogin = login.trim();
        String cleanCpf = cleanLogin.replaceAll("\\D", "");

        String searchCpf = cleanCpf.isEmpty() ? "" : cpfHasher.hash(cleanCpf);

        User userSearched = authPublicApi.findByEmailOrCpf(cleanLogin, searchCpf)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas para o login fornecido"));

        return new UserPrincipal(userSearched);
    }
}
