package net.centroweg.gerenciamentocompras.modules.request.service.status;

import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;

import java.util.List;

public interface StatusService {

    StatusResponse createStatus (StatusRequest statusRequest);
    StatusResponse findStatusById (Long id);
    List<StatusResponse> findAllStatus ();
}
