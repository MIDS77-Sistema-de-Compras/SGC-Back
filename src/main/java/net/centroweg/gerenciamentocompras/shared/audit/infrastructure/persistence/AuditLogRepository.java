package net.centroweg.gerenciamentocompras.shared.audit.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {


}
