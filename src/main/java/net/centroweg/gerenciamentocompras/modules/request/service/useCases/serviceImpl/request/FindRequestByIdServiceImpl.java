package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindRequestByIdServiceImpl {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public RequestResponse findRequestById(Long id, UserPrincipal userPrincipal) {
        boolean isOwner = false;

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());

        for(User u: request.getCreatedByUsers()){
            if(u.getEmail().equals(userPrincipal.getUsername())){
                isOwner=true;
                break;
            }
        }
        if(!isOwner){
            throw new AcessDeniedException();
        }

        return requestMapper.toDTO(request);
    }
}
