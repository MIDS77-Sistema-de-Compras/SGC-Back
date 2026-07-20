package net.centroweg.gerenciamentocompras.shared.audit.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.request.AuditLogFilterRequest;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response.AuditLogDTOResponse;
import net.centroweg.gerenciamentocompras.shared.audit.service.usecases.serviceIntrf.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final ListAuditLogAll listAuditLogAll;

    @Override
    public Page<AuditLogDTOResponse> findAll(AuditLogFilterRequest filter, Pageable pageable) {
        return listAuditLogAll.findAll(filter, pageable);
    }
}