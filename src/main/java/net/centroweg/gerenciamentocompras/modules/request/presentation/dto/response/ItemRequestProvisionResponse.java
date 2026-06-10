package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;

public record ItemRequestProvisionResponse(
    Long id,
    Request request,
    Provision provision,
    Status status,
    String additionalInformation
) {}
