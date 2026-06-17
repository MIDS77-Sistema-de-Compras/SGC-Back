package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;


import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;

import java.util.List;

public interface RequestService {

    RequestResponse createRequest(RequestRequest request);
    List<RequestResponse> findAllRequest(UserPrincipal userPrincipal);
    RequestResponse findRequestById(Long id, UserPrincipal userPrincipal);
    RequestResponse updateRequest(RequestRequest request, Long id, UserPrincipal userPrincipal);
    void deleteRequest(Long id, UserPrincipal userPrincipal);
    RequestResponse updateFeedback(UpdateFeedback feedback, Long id);
}
