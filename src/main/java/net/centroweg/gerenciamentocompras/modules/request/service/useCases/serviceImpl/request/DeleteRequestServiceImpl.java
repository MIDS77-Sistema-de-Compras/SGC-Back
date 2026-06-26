package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestAlreadyApprovedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRequestServiceImpl {

    private final RequestRepository repository;
    private final CurrentUserService currentUserService;
    private final RequestBusinessRuleValidator validator;

    public void deleteRequest(Long id){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isOwner = false;
        Request request = repository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());

        User currentUser = currentUserService.getCurrentUser();
        validator.validateCanInactivate(request, currentUser);

        for(User u: request.getCreatedByUsers()){
            if(u.getEmail().equals(userPrincipal.getUsername())){
                isOwner=true;
                break;
            }
        }
        if(!isOwner){
            throw new AcessDeniedException();
        }

        if(request.getStatus().getName().equalsIgnoreCase("Aprovado")){
            throw new RequestAlreadyApprovedException();
        }

        request.setActive(false);
        repository.save(request);
    }
}
