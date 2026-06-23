package net.centroweg.gerenciamentocompras.modules.request.service.mapper.status;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import org.springframework.stereotype.Component;
/**
 * Implementação responsável pelo mapeamento
 * entre entidade Status e seus DTOs.
 *
 * <p>Realiza a conversão entre os objetos utilizados
 * na camada de apresentação e domínio.</p>
 *
 * @author André
 * @since 1.0
 */
@Component
public class StatusMapperImpl implements IStatusMapper {
    /**
     * Converte um DTO de requisição em entidade Status.
     *
     * @param statusRequest dados recebidos na requisição
     * @return entidade Status convertida
     */
    public Status toEntity(StatusRequest statusRequest) {
        return new Status(
                statusRequest.name(),
                statusRequest.description()
        );
    }
    /**
     * Converte uma entidade Status em DTO de resposta.
     *
     * @param status entidade Status
     * @return DTO de resposta convertido
     */
    public StatusResponse toResponse(Status status) {
        return new StatusResponse(
                status.getId(),
                status.getName(),
                status.getDescription()
        );
    }
}
