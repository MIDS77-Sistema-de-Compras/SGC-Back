package net.centroweg.gerenciamentocompras.modules.request.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.StatusPublicData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusPublicApiImpl implements StatusPublicApi {

    private final StatusRepository statusRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<StatusPublicData> findById(Long id) {
        return statusRepository.findById(id)
                .map(status -> new StatusPublicData(status.getId(), status.getName()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StatusPublicData> findByName(String name) {
        return statusRepository.findByNameIgnoreCase(name)
                .map(status -> new StatusPublicData(status.getId(), status.getName()));
    }
}
