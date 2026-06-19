package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import org.springframework.stereotype.Service;
/**
 * Serviço responsável pela remoção de status.
 *
 * @author André
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class DeleteStatusService {

    private final StatusRepository statusRepository;
    /**
     * Remove um status pelo identificador.
     *
     * @param id identificador do status
     */
    public void deleteStatus(Long id) {
        statusRepository.findById(id)
                .orElseThrow(StatusNotFoundException::new);

        statusRepository.deleteById(id);
    }

}
