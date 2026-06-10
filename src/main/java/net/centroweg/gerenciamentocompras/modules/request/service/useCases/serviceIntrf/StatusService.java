package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;

import java.util.List;

public interface StatusService {

    StatusResponse createStatus (StatusRequest statusRequest);
    StatusResponse findStatusById (Long id);
    StatusResponse findStatusByName (String name);
    List<StatusResponse> findAllStatus ();
    StatusResponse editStatus (Long id, StatusRequest statusRequest);
    void deleteStatus (Long id);

}
