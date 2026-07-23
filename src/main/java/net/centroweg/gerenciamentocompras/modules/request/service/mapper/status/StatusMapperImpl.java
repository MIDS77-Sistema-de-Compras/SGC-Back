package net.centroweg.gerenciamentocompras.modules.request.service.mapper.status;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pela conversão entre a entidade({@link Status}) e seus DTOs de entrada({@link StatusRequest}) e saída({@link StatusResponse}).
 */
@Component
public class StatusMapperImpl implements IStatusMapper {

    /**
     * Converte um DTO de entrada do status em uma entidade status.
     * @param statusRequest dados do status.
     * @return dados convertidos para entidade.
     */
    public Status toEntity(StatusRequest statusRequest) {
        return new Status(
                statusRequest.name(),
                statusRequest.description()
        );
    }

    /**
     * Converte uma entidade status em um DTO de saída do status.
     * @param status entidade com os dados do status.
     * @return dados convertidos para DTO de saída.
     */
    public StatusResponse toResponse(Status status) {
        return new StatusResponse(
                status.getId(),
                status.getName(),
                status.getDescription()
        );
    }
}
