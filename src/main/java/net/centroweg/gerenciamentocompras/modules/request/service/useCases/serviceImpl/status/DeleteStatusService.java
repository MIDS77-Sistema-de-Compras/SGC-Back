package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;

/**
 * Caso de uso responsável por remover um {@link Status}.
 */
@Service
@RequiredArgsConstructor
public class DeleteStatusService {

    private final StatusRepository statusRepository;

    /**
     * Remove um status do banco de dados.
     * @param id identificador do status.
     * @throws StatusNotFoundException caso nenhum status seja encontrado.
     */
    public void deleteStatus(Long id) {
        statusRepository.findById(id)
                .orElseThrow(StatusNotFoundException::new);

        statusRepository.deleteById(id);
    }

}
