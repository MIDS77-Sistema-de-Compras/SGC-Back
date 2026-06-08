package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteStatusService {

    private final StatusRepository statusRepository;

    public void deleteStatus(Long id) {
        statusRepository.findById(id)
                .orElseThrow(StatusNotFoundException::new);

        statusRepository.deleteById(id);
    }

}
