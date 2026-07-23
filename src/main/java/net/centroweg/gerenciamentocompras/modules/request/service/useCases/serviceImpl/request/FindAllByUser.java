package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.*;
import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.requestDateBetween;

/**
 * Caso de uso responsável por listar uma {@link Request} pelo usuário autenticado e filtros informados.
 */
@RequiredArgsConstructor
@Service
public class FindAllByUser {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    /**
     * Lista todas as solicitações cadastradas no banco de dados associadas ao usuário autenticado e aos filtros informados.
     * @param filter dados de filtro da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     * @return lista com todas as solicitações encontradas, caso exista.
     */
    @Transactional(readOnly = true)
    public List<RequestResponse> findAllByUser(RequestFilterRequest filter, UserPrincipal userPrincipal) {
        Specification<Request> specification = Specification
                .where(createdByUser(userPrincipal.getUsername()))
                .and(crCodeContain(filter.crCode()))
                .and(statusNameEquals(filter.statusName()))
                .and(supervisorNameContain(filter.supervisorName()))
                .and(requestDateBetween(filter.startDate(), filter.endDate()));

        return requestRepository.findAll(specification)
                .stream()
                .map(requestMapper::toDTO)
                .toList();
    }
}