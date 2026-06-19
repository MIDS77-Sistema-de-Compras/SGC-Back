package net.centroweg.gerenciamentocompras.modules.request.service.mapper.status;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
/**
 * Interface responsável pelo mapeamento entre
 * entidade, DTO de requisição e DTO de resposta
 * da entidade Status.
 *
 * <p>Define os métodos de conversão utilizados
 * na comunicação entre as camadas da aplicação.</p>
 *
 * @author André
 * @since 1.0
 */
public interface IStatusMapper {
    /**
     * Converte um DTO de requisição em entidade.
     *
     * @param statusRequest dados recebidos na requisição
     * @return entidade Status convertida
     */
    public Status toEntity (StatusRequest statusRequest);

    /**
     * Converte uma entidade em DTO de resposta.
     *
     * @param status entidade Status
     * @return DTO de resposta convertido
     */
    public StatusResponse toResponse (Status status);

}
