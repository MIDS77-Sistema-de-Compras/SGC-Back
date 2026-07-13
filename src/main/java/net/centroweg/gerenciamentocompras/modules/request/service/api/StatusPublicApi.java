package net.centroweg.gerenciamentocompras.modules.request.service.api;

import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.StatusPublicData;

import java.util.Optional;

/** Porta publica para consulta imutavel de status por outros modulos. */
public interface StatusPublicApi {

    Optional<StatusPublicData> findById(Long id);

    Optional<StatusPublicData> findByName(String name);
}
