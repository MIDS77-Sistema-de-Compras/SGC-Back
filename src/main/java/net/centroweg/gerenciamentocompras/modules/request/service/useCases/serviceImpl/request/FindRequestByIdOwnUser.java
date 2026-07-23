package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Caso de uso responsável por buscar uma {@link Request} pelo seu identificador e pelo próprio usuário.
 */
@Service
@RequiredArgsConstructor
public class FindRequestByIdOwnUser {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    /**
     * Busca uma solicitação no banco de dados pelo ID informado e associada ao próprio usuário.
     * @param id identificador da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     * @return solicitação encontrada, caso exista.
     * @throws RequestNotFoundException caso nenhuma solicitação seja encontrada.
     * @throws AcessDeniedException caso o usuário não seja o dono da solicitação.
     */
    public RequestResponse findRequestByIdOwnUser(Long id, UserPrincipal userPrincipal) {
        var request = requestRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);

        boolean isOwner = request.getCreatedByUsers().stream()
                .anyMatch(user -> user.getEmail().equals(userPrincipal.getUsername()));

        if (!isOwner) {
            throw new AcessDeniedException();
        }

        return requestMapper.toDTO(request);
    }
}
