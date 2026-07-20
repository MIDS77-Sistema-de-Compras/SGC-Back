package net.centroweg.gerenciamentocompras.modules.request.service.api;

import java.util.Optional;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;

/** Porta publica para consulta imutavel de status por outros modulos. */
public interface StatusPublicApi {

    Optional<Status> findById(Long id);

    Optional<Status> findByName(String name);
}
