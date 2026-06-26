package net.centroweg.gerenciamentocompras.modules.auth.service.api;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

import java.util.Optional;

public interface AuthPublicApi {

    Optional<User> findByEmailOrCpf(String email, String cpf);
    Boolean existsByEmail(String email);
}
