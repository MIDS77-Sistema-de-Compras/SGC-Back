package net.centroweg.gerenciamentocompras.shared.audit.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuditLogPublicApiImpl implements AuditLogPublicApi{

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public User findByUserName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Request findByRequestId(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);
    }
}
