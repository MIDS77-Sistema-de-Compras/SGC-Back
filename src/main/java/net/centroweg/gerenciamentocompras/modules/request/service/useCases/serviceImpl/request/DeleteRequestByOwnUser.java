package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestAlreadyApprovedException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRequestByOwnUser {

    private final RequestRepository requestRepository;

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
