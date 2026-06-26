package net.centroweg.gerenciamentocompras.modules.auth.domain.entity;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userEntity.getRole().getRole()));
    }

    @Override
    public @Nullable String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

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
