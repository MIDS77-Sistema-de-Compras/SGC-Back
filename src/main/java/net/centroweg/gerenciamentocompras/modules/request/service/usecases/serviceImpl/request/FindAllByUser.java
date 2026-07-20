package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.crCodeContain;
import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.createdByUser;
import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.requestDateBetween;
import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.statusNameEquals;
import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.supervisorNameContain;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;

@RequiredArgsConstructor
@Service
public class FindAllByUser {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Transactional(readOnly = true)
    public Page<RequestResponse> findAllByUser(RequestFilterRequest filter, UserPrincipal userPrincipal, Pageable pageable) {
        Specification<Request> specification = Specification
                .where(createdByUser(userPrincipal.getUsername()))
                .and(crCodeContain(filter.crCode()))
                .and(statusNameEquals(filter.statusName()))
                .and(supervisorNameContain(filter.supervisorName()))
                .and(requestDateBetween(filter.startDate(), filter.endDate()));



        return requestRepository.findAll(specification, pageable)
                .map(requestMapper::toDTO);
    }
}