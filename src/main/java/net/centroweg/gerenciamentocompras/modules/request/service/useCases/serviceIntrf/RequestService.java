package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;


import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.CreateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;

import java.util.List;

public interface RequestService {

    RequestResponse createRequest(CreateRequestRequest request);
    List<RequestResponse> findAllRequest();
    RequestResponse findRequestById(Long id);
    RequestResponse updateRequest(CreateRequestRequest request, Long id);
    void deleteRequest(Long id);
}
