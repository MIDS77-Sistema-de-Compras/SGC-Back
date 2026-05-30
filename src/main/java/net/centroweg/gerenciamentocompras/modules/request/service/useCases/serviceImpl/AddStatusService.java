package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.IStatusMapper;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelo cadastro de novos status.
 *
 * @author André
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AddStatusService {

    private final IStatusMapper statusMapper;
    private final StatusRepository statusRepository;
    /**
     * Realiza o cadastro de um novo status.
     *
     * @param statusRequest dados do status
     * @return status cadastrado
     */
    public StatusResponse addStatus (StatusRequest statusRequest) {
        if (statusRepository.existsByName(statusRequest.name())) {
            throw new StatusAlreadyExistsException();
        }

        Status status = statusRepository.save(statusMapper.toEntity(statusRequest));

        return statusMapper.toResponse(status);
    }
}
