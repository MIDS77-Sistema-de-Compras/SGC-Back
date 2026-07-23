package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestAlreadyApprovedException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por remover uma {@link Request} pelo próprio usuário.
 */
@Service
@RequiredArgsConstructor
public class DeleteRequestByOwnUser {

    private final RequestRepository requestRepository;

    /**
     * Remove uma solicitação do banco de dados pelo próprio usuário.
     * @param id identificador da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     * @throws UserNotFoundException caso nenhuma solicitação seja encontrada.
     * @throws AcessDeniedException caso o usuário não seja o dono da solicitação.
     * @throws RequestAlreadyApprovedException caso a solicitação já esteja aprovada.
     */
    public void deleteRequestByOwnUser(long id, UserPrincipal userPrincipal){
        Request request = requestRepository.findById(id).orElseThrow(UserNotFoundException::new);

        boolean isOwner = request.getCreatedByUsers().stream()
                .anyMatch(user -> user.getEmail().equals(userPrincipal.getUsername()));

        if (!isOwner) {
            throw new AcessDeniedException();
        }

        if (request.getStatus().getName().equalsIgnoreCase("Aprovado")) {
            throw new RequestAlreadyApprovedException();
        }

        request.setActive(false);
        requestRepository.save(request);
    }

}
