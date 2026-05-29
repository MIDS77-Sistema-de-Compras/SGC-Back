package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;


import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.CreateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;

public interface RequestService {

    RequestResponse createRequest(CreateRequestRequest request);
}
