package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRequestServiceImpl {

    private final RequestRepository repository;

    public void deleteRequest(Long id){
        repository.deleteById(id);
    }
}
