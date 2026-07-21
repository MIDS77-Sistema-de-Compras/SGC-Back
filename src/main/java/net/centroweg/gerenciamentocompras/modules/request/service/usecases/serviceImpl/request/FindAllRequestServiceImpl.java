package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;

import static net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification.RequestSpecification.*;

@Service
@RequiredArgsConstructor
public class FindAllRequestServiceImpl {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    /**
     * Status que geram trabalho de compra: os 3 de entrada (finalizados pelo
     * supervisor/coordenador, exceto Recusado) + todo o ciclo que o próprio comprador
     * vai atribuindo dali pra frente. Mantém o comprador vendo a solicitação mesmo
     * depois de ele já ter avançado o status dela (ex.: Em atendimento -> Entregue).
     */
    private static final Set<String> COMPRADOR_VISIBLE_STATUSES = Set.of(
            "aprovado",
            "auto_aprovado",
            "parcialmente_aprovada",
            "em atendimento",
            "atrasada",
            "recebimento_parcial",
            "fundo_rotativo",
            "cd_central",
            "solicitado_portal",
            "parcialmente_atendida",
            "entregue",
            "pedido cancelado"
    );

    @Transactional(readOnly = true)
    public Page<RequestResponse> findAllRequest(RequestFilterRequest filter, Pageable pageable,UserPrincipal userPrincipal) {

        Specification<Request> statusSpecification = isComprador(userPrincipal)
                ? statusNameIn(COMPRADOR_VISIBLE_STATUSES)
                : statusNameEquals(filter.statusName());

        Specification<Request> specification = Specification.allOf(
                crCodeContain(filter.crCode()),
                statusSpecification,
                supervisorNameContain(filter.supervisorName()),
                requestDateBetween(
                    filter.startDate(),
                    filter.endDate()
                )

        );

        Page<Request> page = requestRepository.findAll(specification, pageable);

        requestRepository.initializeForResponse(
                page.getContent().stream().map(Request::getId).toList()
        );

        return page.map(requestMapper::toDTO);
    }

    private boolean isComprador(UserPrincipal userPrincipal) {
        return userPrincipal != null && userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Authorities.COMPRADOR));
    }
}

