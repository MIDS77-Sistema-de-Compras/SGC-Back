package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.status.IStatusMapper;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;

/**
 * Caso de uso responsável por buscar um {@link Status} pelo seu nome.
 */
@Service
@RequiredArgsConstructor
public class FindStatusByNameService {

    private final StatusRepository repository;
    private final IStatusMapper mapper;

    /**
     * Busca um status no banco de dados pelo nome informado.
     * @param name nome do status.
     * @return status encontrado, caso exista.
     * @throws StatusNotFoundException caso nenhum status seja encontrado.
     */
    public StatusResponse findStatusByName(String name){
        return mapper.toResponse(repository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new StatusNotFoundException()));
    }
}
