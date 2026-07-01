package net.centroweg.gerenciamentocompras.shared.audit.service.usecase.impl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import net.centroweg.gerenciamentocompras.shared.audit.infrastructure.persistence.AuditLogRepository;
import net.centroweg.gerenciamentocompras.shared.audit.infrastructure.specification.AuditLogSpecification;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.request.AuditLogFilterRequest;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response.AuditLogDTOResponse;
import net.centroweg.gerenciamentocompras.shared.audit.service.mapper.AuditLogMapper;
import net.centroweg.gerenciamentocompras.shared.audit.service.usecase.interfaces.AuditLogService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListAuditLogAll {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final AuditLogSpecification logSpecification;

    public List<AuditLogDTOResponse> findAll(AuditLogFilterRequest filter) {
        Specification<AuditLog> specification = Specification.allOf(
                logSpecification.typeActionEquals(filter.typeAction()),
                logSpecification.agentUserEmailEquals(filter.agentEmail()),
                logSpecification.auditLogDateBetween(
                        filter.startDate().toLocalDate(),
                        filter.endDate().toLocalDate()
                )
        );

        return auditLogRepository.findAll().stream()
                .map(auditLogMapper::toResponse)
                .toList();
    }
}