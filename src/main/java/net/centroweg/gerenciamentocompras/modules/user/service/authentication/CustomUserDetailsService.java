package net.centroweg.gerenciamentocompras.modules.user.service.authentication;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.adapter.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserPrincipal userPrincipal;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userSearched = userRepository.findByEmailOrCpf(username, username)
                .orElseThrow( () -> new UsernameNotFoundException("Não há usuário com as credenciais digitadas para autenticação"));


        return null;
    }
}
