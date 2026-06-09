package net.centroweg.gerenciamentocompras.modules.auth.service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.service.api.AuthPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthPublicApi authPublicApi;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        String cleanLogin = login.trim();
        String cleanCpf = cleanLogin.replaceAll("\\D", "");

        User userSearched = authPublicApi.findByEmailOrCpf(cleanLogin, cleanCpf)
                .orElseThrow( () -> new UsernameNotFoundException("Credenciais inválidas para o login fornecido"));

        return new UserPrincipal(userSearched);
    }
}
