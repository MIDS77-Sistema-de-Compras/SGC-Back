package net.centroweg.gerenciamentocompras.modules.request.service.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.IStatusMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindStatusByIdService {

    private final IStatusMapper statusMapper;
    private final StatusRepository statusRepository;

    public StatusResponse findStatusById (Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(StatusNotFoundException::new);

        return statusMapper.toResponse(status);
    }

}
