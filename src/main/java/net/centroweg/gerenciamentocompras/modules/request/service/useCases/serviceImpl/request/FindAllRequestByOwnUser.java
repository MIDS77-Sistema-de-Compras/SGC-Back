package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllRequestByOwnUser {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public List<RequestResponse> findAllRequestByOwnUser(UserPrincipal userPrincipal) {

        List<RequestResponse> requestResponses = new ArrayList<>();

        for (Request request : requestRepository.findAll()) {
            for (User u : request.getCreatedByUsers()) {
                if (u.getEmail().equals(userPrincipal.getUsername())) {
                    requestResponses.add(requestMapper.toDTO(request));
                    break;
                }
            }
        }
        return requestResponses;
    }

}
