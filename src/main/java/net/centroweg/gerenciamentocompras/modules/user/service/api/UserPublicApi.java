package net.centroweg.gerenciamentocompras.modules.user.service.api;

import java.util.Optional;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

public interface UserPublicApi {

    Boolean existsByEmail(String email);
    Optional<User> findByEmailOrCpf(String email, String cpf);
    
}
