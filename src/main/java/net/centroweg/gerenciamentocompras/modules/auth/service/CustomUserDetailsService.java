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

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthPublicApi authPublicApi;
    private final CpfHasher cpfHasher;

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
