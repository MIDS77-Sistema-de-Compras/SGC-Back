package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;


import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;

import java.util.List;

public interface RequestService {

    RequestResponse createRequest(RequestRequest request);
    List<RequestResponse> findAllRequest();
    RequestResponse findRequestById(Long id);
    RequestResponse updateRequest(RequestRequest request, Long id);
    void deleteRequest(Long id);
    RequestResponse updateFeedback(UpdateFeedback feedback, Long id);
}
