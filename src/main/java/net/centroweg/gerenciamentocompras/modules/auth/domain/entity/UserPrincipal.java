package net.centroweg.gerenciamentocompras.modules.auth.domain.entity;

import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Classe que representa o usuário já autenticado.
 * Implementa a classe {@link UserDetails} que define quais informações o sistema precisa conhecer sobre esse usuário.
 */
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User userEntity;

    /**
     * Permissões do usuário.
     * @return permissão do usuário para verificar acesso e nível de acesso.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userEntity.getRole().getRole()));
    }

    /**
     * Senha criptografada do usuário.
     * @return senha criptografada para comparar com a senha digitada.
     */
    @Override
    public @Nullable String getPassword() {
        return userEntity.getPassword();
    }

    /**.
     * Identificador utilizado no login.
     * @return e-mail ou CPF que foi utilizado no login.
     */
    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    /**
     * Indica se a conta do usuário expirou.
     * @return booleano para identificar se a conta expirou(true) ou não(false).
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Indica se a conta do usuário não foi bloqueada.
     * @return booleano para identificar se a conta foi bloqueada(true) ou não(false).
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Verifica se a senha expirou.
     * @return booleano para identificar se a senha expirou(true) ou não(false).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Verifica se o usuário está ativo no sistema.
     * @return booleano para identificar se está ativo(true) ou não(false).
     */
    @Override
    public boolean isEnabled() {
        return userEntity.getActive();
    }

    public Long getId() {
        return userEntity.getId();
    }

    public String getCpf(){
        return userEntity.getCpf();
    }

    public String getName(){
        return userEntity.getName();
    }
}
