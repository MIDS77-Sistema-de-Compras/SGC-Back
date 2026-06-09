package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

import java.io.ObjectInputFilter.Status;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

public record ItemRequestProvisionResponse(
    Long id,
    Request request,
    Provision provision,
    Status status,
    String additionalInformation
) {}
