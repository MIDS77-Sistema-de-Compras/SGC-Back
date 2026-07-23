package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.status.IStatusMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela criação de um {@link Status}.
 */
@Service
@RequiredArgsConstructor
public class AddStatusService {

    private final IStatusMapper statusMapper;
    private final StatusRepository statusRepository;

    /**
     * Cria e persiste um novo status no banco de dados.
     * @param statusRequest dados do status.
     * @return status criado.
     * @throws StatusAlreadyExistsException caso já exista um status com o mesmo nome.
     */
    public StatusResponse addStatus (StatusRequest statusRequest) {
        if (statusRepository.existsByName(statusRequest.name())) {
            throw new StatusAlreadyExistsException();
        }

        Status status = statusRepository.save(statusMapper.toEntity(statusRequest));

        return statusMapper.toResponse(status);
    }
}
