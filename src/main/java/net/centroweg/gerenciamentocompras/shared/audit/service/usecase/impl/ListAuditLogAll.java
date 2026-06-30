package net.centroweg.gerenciamentocompras.shared.audit.service.usecase.impl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.audit.infrastructure.persistence.AuditLogRepository;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response.AuditLogDTOResponse;
import net.centroweg.gerenciamentocompras.shared.audit.service.mapper.AuditLogMapper;
import net.centroweg.gerenciamentocompras.shared.audit.service.usecase.interfaces.AuditLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListAuditLogAll {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public List<AuditLogDTOResponse> findAll() {
        return auditLogRepository.findAll().stream()
                .map(auditLogMapper::toResponse)
                .toList();
    }
}