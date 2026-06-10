package net.centroweg.gerenciamentocompras.modules.request.service.mapper.status;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;

public interface IStatusMapper {

    public Status toEntity (StatusRequest statusRequest);

    public StatusResponse toResponse (Status status);

}
