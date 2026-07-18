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
import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.requestDateBetween;
import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.statusNameEquals;
import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.supervisorNameContain;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;

@Service
@RequiredArgsConstructor
public class FindAllRequestServiceImpl {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    private static final String APPROVED_STATUS = "Aprovado";

    @Transactional(readOnly = true)
    public Page<RequestResponse> findAllRequest(RequestFilterRequest filter, Pageable pageable,UserPrincipal userPrincipal) {

        String statusName = isComprador(userPrincipal) ? APPROVED_STATUS : filter.statusName();

        Specification<Request> specification = Specification.allOf(
                crCodeContain(filter.crCode()),
                statusNameEquals(statusName),
                supervisorNameContain(filter.supervisorName()),
                requestDateBetween(
                    filter.startDate(),
                    filter.endDate()
                )

        );

        return requestRepository.findAll(specification, pageable)
                .map(requestMapper::toDTO);
    }

    private boolean isComprador(UserPrincipal userPrincipal) {
        return userPrincipal != null && userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Authorities.COMPRADOR));
    }
}

