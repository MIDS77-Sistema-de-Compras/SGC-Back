package net.centroweg.gerenciamentocompras.modules.request.service.mapper;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import org.springframework.stereotype.Component;

@Component
public class StatusMapperImpl implements IStatusMapper {

    public Status toEntity(StatusRequest statusRequest) {
        return new Status(
                statusRequest.name(),
                statusRequest.description()
        );
    }

    public StatusResponse toResponse(Status status) {
        return new StatusResponse(
                status.getId(),
                status.getName(),
                status.getDescription()
        );
    }
}
