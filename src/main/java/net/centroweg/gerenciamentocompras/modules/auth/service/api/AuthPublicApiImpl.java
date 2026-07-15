package net.centroweg.gerenciamentocompras.modules.auth.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthPublicApiImpl implements AuthPublicApi{

    private final UserPublicApi userPublicApi;

    @Override
    public Optional<User> findByEmailOrCpf(String email, String cpf) {
        return userPublicApi.findByEmailOrCpf(email, cpf);
    }

    @Override
    public Boolean existsByEmail(String email){
        return userPublicApi.existsByEmail(email);
    }
}
