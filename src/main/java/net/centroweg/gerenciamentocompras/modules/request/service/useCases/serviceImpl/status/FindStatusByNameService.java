package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.status.IStatusMapper;
import org.springframework.stereotype.Service;
/**
 * Serviço responsável pela busca de status por nome.
 *
 * @author André
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class FindStatusByNameService {

    private final StatusRepository repository;
    private final IStatusMapper mapper;
    /**
     * Busca um status pelo nome.
     *
     * @param name nome do status
     * @return status encontrado
     */
    public StatusResponse findStatusByName(String name){
        return mapper.toResponse(repository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new StatusNotFoundException()));
    }
}
