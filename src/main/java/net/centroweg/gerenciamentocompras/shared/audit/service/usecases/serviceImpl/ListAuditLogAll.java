package net.centroweg.gerenciamentocompras.shared.audit.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import net.centroweg.gerenciamentocompras.shared.audit.infrastructure.persistence.AuditLogRepository;
import net.centroweg.gerenciamentocompras.shared.audit.infrastructure.specification.AuditLogSpecification;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.request.AuditLogFilterRequest;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response.AuditLogDTOResponse;
import net.centroweg.gerenciamentocompras.shared.audit.service.mapper.AuditLogMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListAuditLogAll {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final AuditLogSpecification logSpecification;

    /**
     * Lista os registros de auditoria mais recentes que atendem ao filtro.
     *
     * <p>A tabela de auditoria cresce a cada ação de escrita no sistema; por isso a
     * consulta é limitada aos {@code limit} registros mais recentes (ordenados por
     * {@code timestamp} decrescente), evitando carregar o histórico inteiro na tela de logs.</p>
     *
     * @param filter filtros opcionais (tipo de ação, e-mail do agente, período)
     * @return os registros mais recentes, do mais novo para o mais antigo
     */
    public Page<AuditLogDTOResponse> findAll(AuditLogFilterRequest filter, Pageable pageable) {
        Specification<AuditLog> specification = Specification.allOf(
                logSpecification.typeActionEquals(filter.typeAction()),
                logSpecification.agentUserEmailEquals(filter.agentEmail()),
                logSpecification.auditLogDateBetween(
                        (filter.startDate() != null) ? filter.startDate().toLocalDate() : null,
                        (filter.endDate() != null) ? filter.endDate().toLocalDate() : null
                )
        );

        return auditLogRepository.findAll(specification, pageable)
                .map(auditLogMapper::toResponse);
    }
}