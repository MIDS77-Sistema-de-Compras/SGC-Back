package net.centroweg.gerenciamentocompras.shared.audit.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {

    /**
     * Sobrescreve a listagem paginada para carregar as associações usadas pelo mapper
     * (agente, role do agente, alvo e solicitação) em uma única query com JOIN,
     * evitando o N+1 de queries por registro na tela de auditoria.
     */
    @Override
    @EntityGraph(attributePaths = {"userAgent", "userAgent.role", "userTarget", "request"})
    Page<AuditLog> findAll(Specification<AuditLog> spec, Pageable pageable);

    /**
     * Caminho rápido da listagem sem filtros: retorna os registros mais recentes
     * com as associações em JOIN e sem a query de count da paginação
     * (o retorno em List, diferente de Page, não dispara o count).
     */
    @EntityGraph(attributePaths = {"userAgent", "userAgent.role", "userTarget", "request"})
    List<AuditLog> findAllByOrderByTimestampDesc(Pageable pageable);
}
