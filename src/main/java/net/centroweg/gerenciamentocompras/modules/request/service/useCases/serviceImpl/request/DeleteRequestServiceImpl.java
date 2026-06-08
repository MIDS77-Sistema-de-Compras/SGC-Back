package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestAlreadyApprovedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRequestServiceImpl {

    private final RequestRepository repository;

    public void deleteRequest(Long id){
        Request request = repository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());
        if(request.getStatus().getName().equalsIgnoreCase("Aprovado")){
            throw new RequestAlreadyApprovedException();
        }
        request.setActive(false);
        repository.save(request);
    }
}
