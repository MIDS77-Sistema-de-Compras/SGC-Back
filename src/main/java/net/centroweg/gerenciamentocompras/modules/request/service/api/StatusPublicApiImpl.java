package net.centroweg.gerenciamentocompras.modules.request.service.api;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;

@Service
@RequiredArgsConstructor
public class StatusPublicApiImpl implements StatusPublicApi {

    private final StatusRepository statusRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Status> findById(Long id) {
        return statusRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Status> findByName(String name) {
        return statusRepository.findByNameIgnoreCase(name);
    }
}
