package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.*;

/**
 * Caso de uso responsável por listar uma {@link Request} pelos filtros informados.
 */
@Service
@RequiredArgsConstructor
public class FindAllRequestServiceImpl {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    /**
     * Lista todas as solicitações cadastradas no banco de dados associadas aos filtros informados.
     * @param filter dados de filtro da solicitação.
     * @return lista com todas as solicitações encontradas, caso exista.
     */
    @Transactional(readOnly = true)
    public List<RequestResponse> findAllRequest(RequestFilterRequest filter) {
        Specification<Request> specification = Specification.allOf(
                crCodeContain(filter.crCode()),
                statusNameEquals(filter.statusName()),
                supervisorNameContain(filter.supervisorName()),
                requestDateBetween(
                    filter.startDate(),
                    filter.endDate()
                )

        );

        return requestRepository.findAll(specification)
                .stream()
                .map(requestMapper::toDTO)
                .toList();
    }
}

