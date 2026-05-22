package net.centroweg.gerenciamentocompras.modules.request.service.status;

import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;

public interface StatusService {

    StatusResponse createStatus (StatusRequest statusRequest);

}
